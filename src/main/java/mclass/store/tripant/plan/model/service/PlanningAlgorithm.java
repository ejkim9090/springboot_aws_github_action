package mclass.store.tripant.plan.model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mclass.store.tripant.place.domain.AreaPointEntity;
import mclass.store.tripant.plan.domain.CalendarPlanEntity;
import mclass.store.tripant.plan.domain.PlacePointEntity;
import mclass.store.tripant.plan.domain.PlanDate;
import mclass.store.tripant.plan.domain.Spot;
import mclass.store.tripant.plan.domain.Stay;
import mclass.store.tripant.plan.model.repostiory.PlanRepository;

@Service
public class PlanningAlgorithm {
	@Autowired
	private PlanRepository planRepository;
	static int V;

	// 그래프를 표현 할 List
	static List<List<Node>> graph = new ArrayList<>();

	static List<PlacePointEntity> stayPointList = new ArrayList<>();
	static List<PlacePointEntity> spotPointList = new ArrayList<>();

	static List<PlacePointEntity> errorPointList = new ArrayList<>();
	static List<PlacePointEntity> resultPoinList = new ArrayList<>();

	static class Node implements Comparable<Node> {
		private int index;
		private int weight;

		public Node(int index, int weight) {
			this.index = index;
			this.weight = weight;
		}

		@Override
		public int compareTo(Node o) {
			return Integer.compare(this.weight, o.weight);
		}
	}

//	0. 날짜 수 만큼 저장될 리스트 생성
//
//	1. 총 날짜 수로 장소 개수 나누기
//	1-1. 만약 날짜 수  = 나눈 장소 수(1) -> 그냥 출-장-도 로 모든 날짜 배치
//	1-2. 만약 날짜 수  < 나눈 장소 수(2) -> 출과 가까운걸 먼저 배치
//	1-3. 만약 날짜 수  < 나눈 장소 수(3) 
//	      => 출과 가까운거 빼냄 , 도와 가까운 거 빼냄 -> 그대로 배치
//	1-4. 만약 날짜 수  < 나눈 장소 수(3보다 큼) 
//	      => 출과 가까운거 빼냄 , 도와 가까운 거 빼냄  --> 나머지로 다익스트라
//
//	2. 날짜별 리스트에 각각 결정된 리스트 저장

	public void planningJson(String jsonString, int areaCode) { // planJsonParse
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		CalendarPlanEntity calendarPlan = gson.fromJson(jsonString, CalendarPlanEntity.class);

		// 날짜 별 정보(하루, 숙소)
		List<PlanDate> dateArr = calendarPlan.getDateArr();
		for (int i = 0; i < dateArr.size(); i++) {
			Stay stay = dateArr.get(i).getStay();
			stayPointList.add(new PlacePointEntity(i, stay.getId(), stay.getMapx(), stay.getMapy(), 0));
		}
		System.out.println(stayPointList);

		// 선택 한 장소
		List<Spot> spotArr = calendarPlan.getSpotArr();
		for (int i = 0; i < spotArr.size(); i++) {
			spotPointList.add(new PlacePointEntity(i, spotArr.get(i).getId(), spotArr.get(i).getMapx(),
					spotArr.get(i).getMapy(), 0));
		}
		System.out.println(spotPointList);

		V = spotPointList.size();   // 정점 개수 -> 선택한 장소 수
		int dayN = dateArr.size();  // 여행할 날짜 수
		
//		0. 날짜 수 만큼 저장될 리스트 생성		
		for(int i = 0; i < dayN ; i++) {
			
		}

//		1. 총 날짜 수로 장소 개수 나누기
		int distribute = (int) Math.ceil(V / dayN);
		System.out.println(distribute);
		
//		1-1. 만약 날짜 수  = 나눈 장소 수(1) -> 그냥 출-장-도 로 모든 날짜 배치
//		1-2. 만약 날짜 수  < 나눈 장소 수(2) -> 출과 가까운걸 먼저 배치
//		1-3. 만약 날짜 수  < 나눈 장소 수(3) 
//		      => 출과 가까운거 빼냄 , 도와 가까운 거 빼냄 -> 그대로 배치
//		1-4. 만약 날짜 수  < 나눈 장소 수(3보다 큼) 
//		      => 출과 가까운거 빼냄 , 도와 가까운 거 빼냄  --> 나머지로 다익스트라
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		// 출발지에서 가장 가까운 장소 구하기
		int weight[] = new int[V];
		int minIndex = 0;
		int min = 0;
		AreaPointEntity startPoint = planRepository.selectAreaPoint(areaCode);
		for (int i = 0; i < V; i++) {

			double startx = Double.parseDouble(startPoint.getAreaX());
			double starty = Double.parseDouble(startPoint.getAreaY());
			double endx = Double.parseDouble(spotPointList.get(i).getMapx());
			double endy = Double.parseDouble(spotPointList.get(i).getMapy());

			String durationStr = getduration(startx, starty, endx, endy);
			if (durationStr.equals("길찾기 결과를 찾을 수 없음") || durationStr.equals("")) {
				weight[i] = 10800; // 3시간 //TODO
			} else {
				System.out.println(durationStr);
				weight[i] = Integer.parseInt(durationStr);
			}
		}

		min = weight[0];
		// 가장 가까운 곳이 몇번째에 있는지
		for (int i = 0; i < V; i++) {
			if (min > weight[i]) {
				min = weight[i];
				minIndex = i;
			}
		}
		Collections.swap(spotPointList, 0, minIndex); // 위치 변경

		System.out.println(spotPointList.get(0));

		planning();
	}

	public void planning() { // 정점 세팅 및 알고리즘 실행
		for (int i = 0; i < V; i++) {
			graph.add(new ArrayList<>());
		}
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				if (j != i) {
					PlacePointEntity start = spotPointList.get(i); // 출발 노드
					PlacePointEntity end = spotPointList.get(j); // 도착 노드

					double startx = Double.parseDouble(start.getMapx());
					double starty = Double.parseDouble(start.getMapy());
					double endx = Double.parseDouble(end.getMapx());
					double endy = Double.parseDouble(end.getMapy());

					System.out.println(startx + " : " + starty + " : " + endx + " : " + endy);

					String durationStr = getduration(startx, starty, endx, endy);

					if (durationStr.equals("길찾기 결과를 찾을 수 없음") || durationStr.equals("")) {
						errorPointList.add(start);
					} else {
						System.out.println(durationStr);
						int weight = Integer.parseInt(durationStr);

						graph.get(i).add(new Node(end.getIndex(), weight));
						graph.get(j).add(new Node(start.getIndex(), weight));
					}
				}
			}
		}
		Dijkstra(0);
	}

	private static void Dijkstra(int index) {
		Map<Integer, Integer> result = new HashMap<>();
		PriorityQueue<Node> pq = new PriorityQueue<>();
		int[] distance = new int[V]; // 최단 거리를 저장할 변수

		// distance값 초기화.
		Arrays.fill(distance, Integer.MAX_VALUE);

		// 시작노드값 초기화.
		distance[index] = 0;
		pq.offer(new Node(index, 0));

		while (!pq.isEmpty()) {
			// 큐에서 노드 꺼내기
			Node node = pq.poll();
			int nodeIndex = node.index; // 꺼낸 노드의 인덱스
			int weight = node.weight;
			/**
			 * 큐는 최단거리를 기준으로 오름차순 정렬되고 있습니다. 만약 현재 꺼낸 노드의 거리가 최단거리테이블의 값보다 크다면 해당 노드는 이전에
			 * 방문된 노드이므로, 해당노드와 연결 된 노드를 탐색하지 않고 큐에서 다음 노드를 꺼냅니다.
			 */
			if (weight > distance[nodeIndex]) {
				continue;
			}

			for (Node linkedNode : graph.get(nodeIndex)) {
				if (weight + linkedNode.weight < distance[linkedNode.index]) {
					// 최단 테이블 갱신
					distance[linkedNode.index] = weight + linkedNode.weight;
					// 갱신 된 노드를 우선순위 큐에 넣어
					pq.offer(new Node(linkedNode.index, distance[linkedNode.index]));
				}
			}

			// 결과값 출력
			for (int i = 0; i < V; ++i) {
				if (distance[i] == Integer.MAX_VALUE)
					System.out.print("∞ ");
				else {
					System.out.print(distance[i] + " ");
				}
			}
			System.out.println();

		}

//		
//		int weight[] = new int[V];
//		int minIndex = 0;
//		int min = 0;
//		min = weight[0];
//		// 가장 가까운 곳이 몇번째에 있는지
//		for (int i = 0; i < V; i++) {
//			if (min < weight[i]) {
//				min = weight[i];
//				minIndex = i;
//			}
//		}
//		
//		
//		for (int i = 0; i < V; i++) { // 시간 적은 순서대로 다시 저장
//			int min =  distance[i];
//			for(int j = i+1; j < V; j++) {
//				if(min < distance[j]) {
//					resultPoinList.add(i, new PlacePointEntity(i, spotPointList.get(i).getContentid(),
//							spotPointList.get(i).getMapx(), spotPointList.get(i).getMapy(), distance[i]));
//				}
//				
//			}
//			
//			
//		}
//		

		// 그냥 넣고 swap, index 다시 붙여주기

		System.out.println(spotPointList);
	}

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
		String errorStr = "";
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
				br.close();

				JSONObject jsonObject = new JSONObject(response.toString());
				// JSON 파싱하여 응답 코드 확인
				errorStr = jsonObject.getJSONArray("routes").getJSONObject(0).getString("result_code");
				if (errorStr.equals("0")) { // 정상 호출
					// JSON 파싱하여 duration 값만 추출
					duration = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("summary")
							.getString("duration");
				}
			} else { // HTTP_OK 제외하고 오류 발생한 상황
				return ">>>> 에러났어요 : " + responseCode;
			}
		} catch (Exception e) {
			System.out.println(" >>>>>>>>>>>>>>>>ERROR 길찾기 결과를 찾을 수 없음");
			duration = "길찾기 결과를 찾을 수 없음";
		}
		return duration;
	}
}
