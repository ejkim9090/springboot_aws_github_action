package mclass.store.tripant.plan.model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import mclass.store.tripant.place.domain.PlaceMapEntity;
import mclass.store.tripant.place.domain.PlaceMoveTimeEntity;
import mclass.store.tripant.plan.model.repostiory.TimeRepositoryCrawling;

@Service
public class TimeServiceCrawling {
	@Autowired
	private TimeRepositoryCrawling timeRepository;

	public int deleteAllPlaceMoveTime() {
		return timeRepository.deleteAllPlaceMoveTime();
	}

	public List<PlaceMapEntity> selectPlaceMapList(int areacode) {
		return timeRepository.selectPlaceMapList(areacode);
	}
	
	public int insertPlaceMoveTime(PlaceMoveTimeEntity moveTimeEntity) {
		return timeRepository.insertPlaceMoveTime(moveTimeEntity);
	}
	
	// @Scheduled(cron = "* * 6 * * 1") //매주 일요일 6시에 실행
	public void makeTimeList() {
//		int[] areaCode = { 1, 2, 3, 4, 5, 6, 7, 8, 31, 32, 33, 34, 35, 36, 37, 38, 39 };
//TODO 8
		List<PlaceMapEntity> placeList = timeRepository.selectPlaceMapList(38); // 이동시간 계산할 지역에 포함된 장소 리스트(type =10은 제외하고)

		int depth = 0; // 깊이 0번 부터 탐색 시작
		int n = placeList.size(); // 장소 리스트 크기
		boolean[] visited = new boolean[n]; // 각각 방문체크
		int r = 2; // 2개로 순열 생성
		PlaceMapEntity[] output = new PlaceMapEntity[r]; // 각각 [출발, 도착] 형태로 저장

		List<PlaceMapEntity[]> startendList = new ArrayList<>();// List[출발 정보, 도착 정보]

//		System.out.println("======= " + ((n * n - 1) - n + 1)); // 만들어진 경우의 수 크기
//		경우의 수 구하기
		perm(placeList, output, visited, depth, n, r, startendList);

		/*
		 * 결과 확인 System.out.println(resultList.size()); for(PlaceMapEntity[] i :
		 * resultList) { System.out.print("["); System.out.print(i[0].getContentid() +
		 * " : " +i[1].getContentid()); System.out.println("]"); }
		 */
//		DB에 insert하기 전에 기존내용 지우기
//		timeRepository.deleteAllPlaceMoveTime();
		
//		List<PlaceMoveTimeEntity> timeResultList = null;
//		map에 출발, 도착 보내서 이동시간 DB에 넣기
//		timeResultList = calMoveTime(startendList);
		calMoveTime(startendList);
	}

	// =====================================================map에 출발, 도착 보내서 이동시간 받아오기======================================================================
	// @Async
	private List<PlaceMoveTimeEntity> calMoveTime(List<PlaceMapEntity[]> startendList) {
		List<PlaceMoveTimeEntity> timeResultList = new ArrayList<>();
		int count = 0;
		for (PlaceMapEntity[] i : startendList) {
			double startLng = Double.parseDouble(i[0].getMapx()); // 시작 경도
			double startLat = Double.parseDouble(i[0].getMapy()); // 시작 위도
			double endLng = Double.parseDouble(i[1].getMapx()); // 도착 경도
			double endLat = Double.parseDouble(i[1].getMapy()); // 도착 위도

			String duration = getduration(startLng, startLat, endLng, endLat);

//			System.out.println("startLng : " + startLng + " startLat : " + startLat + " endLng : " + endLng + " endLat : " + endLat);
			
			if(duration != null && !duration.equals("")) { //길찾기 결과를 찾을 수 없을때 null이 반환됨
				PlaceMoveTimeEntity calTime = new PlaceMoveTimeEntity(i[0].getType(), i[0].getContentid(),
						i[1].getType(), i[1].getContentid(), String.valueOf(duration));

//				System.out.println(calTime); //insert 할 정보
				timeRepository.insertPlaceMoveTime(calTime);
				timeResultList.add(calTime);
			}
			count++;
			if(count % 100 == 0) {
				System.out.println(count);
			}
		}
		System.out.println("=============시간 계산이 끝났습니다=================");
		return timeResultList;
	}

	// sts꺼
	@Value("${kakao.map.rest.api}")
	private String apikey;

	// 지점 간 이동시간 구하기
	public String getduration(double startLng, // 시작 경도
			double startLat, // 시작 위도
			double endLng, // 도착 경도
			double endLat // 도착 위도
	) {
		String aurlStr = String.format(
				"https://apis-navi.kakaomobility.com/v1/directions?origin=%f,%f&destination=%f,%f&priority=TIME&summary=true",
				startLng, startLat, endLng, endLat);
		String duration = "";
		try {
			URL url = new URL(aurlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", apikey);
			conn.setRequestProperty("Content-Type", "application/json");

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
//				System.out.println("\n response \n " + response.toString());
				br.close();

				// JSON 파싱하여 duration 값만 추출
				JSONObject jsonObject = new JSONObject(response.toString());
				duration = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("summary")
						.getString("duration");

			} else {
				return ">>>> 에러났어요 : " + responseCode;
			}
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("\n\n >>>>>>>>>>>>>>>>ERROR 확인해주세요<<<<<<<<<<<<<<<<");
//			System.out.println("길찾기 결과를 찾을 수 없음");
		}
		return duration;
	}

	// =====================================================경우의 수 구하기=====================================================
	// @Async
	private void perm(List<PlaceMapEntity> placeList, PlaceMapEntity[] output, boolean[] visited, int depth, int n,
			int r, List<PlaceMapEntity[]> startendList) {
		// 순서를 지키면서 n 개중에서 r 개를 뽑는 경우
		// 사용 예시: perm(arr, output, visited, 0, n, 3);

		if (depth == r) { // depth 의 값이 r 만큼 되면 output 에 들어있는 값을 출력
			// output의 형태는 PlaceMapEntity[출발, 도착]
			startendList.add(output.clone()); // 깊은 복사
			/*
			 * 결과 확인 System.out.print("["); for(PlaceMapEntity i : output) {
			 * System.out.print(i + " : " ); } System.out.println("]");
			 * System.out.println();
			 */
			return;
		}

		for (int i = 0; i < n; i++) {
			if (visited[i] != true) {
				visited[i] = true;
				output[depth] = placeList.get(i);
				perm(placeList, output, visited, depth + 1, n, r, startendList); // depth 의 값은 output에 들어간 숫자의 길이
				visited[i] = false;
			}
		}
	}

}
