package mclass.store.tripant.plan.model.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import mclass.store.tripant.place.domain.PlaceEntity;
import mclass.store.tripant.plan.model.repostiory.PlaceRepositoryCrawling;

@Service
public class PlaceServiceCrawling {
	@Autowired
	private PlaceRepositoryCrawling placeRepository;

	// XML Element 값 꺼내기
	public static String getTagValue(String tag, Element eElement) {
		Node nValue = null;

		NodeList x = eElement.getElementsByTagName(tag);
		Node test = x.item(0);
		NodeList t = null;
		if (test != null) {
			t = test.getChildNodes();
			if ((Node) t.item(0) != null) {
				nValue = (Node) t.item(0);
			}
		}
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

	// tour.rest.api.servicekey 받아오기
	@Value("${tour.rest.api.servicekey}")
	private String tourServiceKey;

	// api 호출
	@Async
	private int getApi(int cId, int aCode , boolean isModifiedData) { // 관광타입, 지역 코드
		int result = 0;
		// api 받아온 시간
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate currentDate =  LocalDate.now();
		String gettime = currentDate.format(dateFormat);
		System.out.println("###########scheduled############" + gettime);

		int numOfRows = 1000;
		int pageNo = 0;
		int totalCount = 0;
		// int maxPageNo = 1;

		// 전체 개수 / 1000 + 1 만큼 반복문 돌림 
		while (pageNo < totalCount / numOfRows + 1) {
			pageNo++;

			String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaBasedList1?" + "numOfRows=%d"
					+ "&pageNo=%d" + "&MobileOS=ETC" + "&MobileApp=TripAnt" + "&_type=xml" + "&arrange=A"
					+ "&contentTypeId=%d" + "&areaCode=%d" + "&serviceKey=", numOfRows, pageNo, cId, aCode);
			url += tourServiceKey;

			try {
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				Document doc = dBuilder.parse(url);

				// 제일 첫번째 태그
				doc.getDocumentElement().normalize();

				// 파싱할 tag
				// 총 개수를 통해 페이지 수를 계산하기
				NodeList nTotalCount = doc.getElementsByTagName("totalCount");
				try {
					totalCount = Integer.parseInt(nTotalCount.item(0).getTextContent());
					System.out.println("totalCount:" + totalCount + ", totalPage " + (totalCount / numOfRows + 1) + ": pageNo:" + pageNo);
				} catch (Exception e) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!! ERROR 2 : " + totalCount);
					e.printStackTrace();
					totalCount = 0;
					continue;
				}

				// 포맷변경 ( 년월일 시분초)
				String yesterday = currentDate.plusDays(-1).format(dateFormat);
				System.out.println("1일 전 : " + yesterday);
				
				// 파싱할 tag
				NodeList nList = doc.getElementsByTagName("item");

				List<PlaceEntity> dtolist = new ArrayList<PlaceEntity>();
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					Element eElement = (Element) nNode;

					String modifiedtime = getTagValue("modifiedtime", eElement);
				
					if(isModifiedData) {
						// 처음 insert 할 때 제외하고는 활성화 해두기(변경시간이 어제시간보다 앞이면)
						if (modifiedtime.compareTo(yesterday) < 0) { 
							continue;
						}
					}

					//만약 주소가 없는 장소라면 insert 하지 않음
					String add1 = getTagValue("addr1", eElement);
					if (add1 == null || add1.length() == 0) {
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!! Add1  NULL");
						continue;
					}
					String add2 = getTagValue("add2", eElement);
					String areacodeS = getTagValue("areacode", eElement); // Integer
					String booktour = getTagValue("booktour", eElement);
					String cat1 = getTagValue("cat1", eElement);
					String cat2 = getTagValue("cat2", eElement);
					String cat3 = getTagValue("cat3", eElement);
					String contentidS = getTagValue("contentid", eElement); // Integer
					String contenttypeidS = getTagValue("contenttypeid", eElement);
					String createtime = getTagValue("createtime", eElement);// 10
					String firstimage = getTagValue("firstimage", eElement);
					String firstimage2 = getTagValue("firstimage2", eElement);
					String cpyrhtDivCd = getTagValue("cpyrhtDivCd", eElement);
					String mapx = getTagValue("mapx", eElement);
					String mapy = getTagValue("mapy", eElement);
					String mlevel = getTagValue("mlevel", eElement);
					String sigungucode = getTagValue("sigungucode", eElement);
					String tel = getTagValue("tel", eElement);
					if (tel != null && tel.length() > 40) {
//					System.out.println("!!!!!!!!!!!!!!!!!!!!!! tel :" + tel);
					}
					tel = (tel != null && tel.length() > 40) ? tel.substring(0, 40) : tel;
					String title = getTagValue("title", eElement);// 20
//						String zipcodeS = getTagValue("zipcode", eElement);  //Integer   

					int areacode = Integer.parseInt(areacodeS);
					int contentid = Integer.parseInt(contentidS);
					int contenttypeid = Integer.parseInt(contenttypeidS);
//						int zipcode = Integer.parseInt(zipcodeS);

					// 관광타입ID(12:관광지, 14:문화시설, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점)
					// type(1:관광지, 2:문화시설, 3:쇼핑, 4:음식점, 5:숙박, 6:캠핑장)
					int type = 10;
					if (contenttypeid == 12) { // 관광지 1
						type = 1;
					} else if (contenttypeid == 14) { // 문화시설 2
						type = 2;
					} else if (contenttypeid == 28) { // 레포츠 -> 캠핑장 6
						if (title.contains("캠핑장")) {
							type = 6;
						}
					} else if (contenttypeid == 32) { // 숙박 5
						type = 5;
					} else if (contenttypeid == 38) { // 쇼핑 3
						type = 3;
					} else if (contenttypeid == 39) { // 음식점 4
						type = 4;
					} else {
						type = 10;
					}

					PlaceEntity dto = new PlaceEntity(type, gettime, contentid, contenttypeid, add1, add2, areacode,
							booktour, cat1, cat2, cat3, firstimage, firstimage2, cpyrhtDivCd, mapx, mapy, createtime,
							mlevel, sigungucode, tel, title, modifiedtime);
					dtolist.add(dto);
				}

				System.out.println("dtolist size : " + dtolist.size());
				if (dtolist.size() > 0) {
				
					 // 200개씩 끊어서 입력
					int limitCount = 200;
					int i = 0;
					do {
						List<PlaceEntity> dataListSlice = new ArrayList<>(dtolist.subList(i, Math.min(i+limitCount,  dtolist.size())));
//						List<PlaceEntity> dataListSlice = dtolist.stream().skip(i).limit(limitCount).collect(Collectors.toList());
						
						int resultPerOnce = placeRepository.insertPlace(dataListSlice);
				    	System.out.println("=====200씩==========" + i + "번째================");
						System.out.println("DB result : " + resultPerOnce);
						result += resultPerOnce;
				        dataListSlice.clear();
				        
				        i = Math.min(i+limitCount,  dtolist.size());
				        
					} while(i < dtolist.size());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("!!!!!!!!!!!!!!!!!!!!!! ERROR 1 ??????");
				e.printStackTrace();
			}
		} // while
		return result;
	}

	@Scheduled(cron = "* * 20 * * *") // 매일 20시에 실행
	// 첫 번째 * 부터 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-6) (0: 일, 1: 월, 2:화, 3:수, 4:목, 5:금, 6:토)
	public int insertPlace() {
		boolean isModifiedData = false;
		
		// 반복문 돌리기(12:관광지, 14:문화시설, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점)
		int result = 0;

		int[] arrType = { 12, 14, 28, 32, 38, 39 }; // 1,2,6camp-10reports,5,3,4
		int arrTypeIdx = 0;
		while (arrTypeIdx < arrType.length) {
			for (int i = 1; ((i <= 8) || (i >= 31 && i <= 39)); /* i++ */) {
//			for(int i= 1; i<2;) {
				result += getApi(arrType[arrTypeIdx], i, isModifiedData);
				i = (i == 8) ? i = 31 : ++i;
			}
			System.out.println(arrTypeIdx + ": result : " + result);
			arrTypeIdx++;
		}

		return result;
	}
}
