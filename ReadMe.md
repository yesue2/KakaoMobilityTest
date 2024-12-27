# Kakao Map Application

## 💻 프로젝트 개요

사용자가 출발지와 도착지를 선택하여 경로를 조회하고, 해당 경로에 대한 교통 정보를 지도에 표시하는 안드로이드 앱입니다.

> - **Kakao Mobility API**를 사용해 경로 정보 제공
> - **Jetpack Compose**를 사용해 UI 구현
> - **Mavericks**를 활용해 상태 관리 구현

---

## 🛠️ 기술 스택

- **언어**: Kotlin
- **UI 프레임워크**: Jetpack Compose
- **상태 관리**: Mavericks (ViewModel 및 상태 관리)
- **네트워크**: Retrofit
- **지도 API**: Kakao Map SDK
- **의존성 관리**: Gradle
- **비동기 처리**: Kotlin Coroutines

---

## 📂 프로젝트 구조

```plaintext
├── data
│   ├── api
│   │   ├── ApiClient.kt              # Retrofit을 사용한 API 클라이언트 설정
│   │   └── KakaoApiService.kt        # Kakao Mobility API 서비스 인터페이스 정의
│   ├── model
│   │   ├── DistanceTimeResponse.kt   # 거리 및 시간 API 응답 데이터 모델
│   │   ├── LocationResponse.kt       # 위치리스트 API 응답 데이터 모델
│   │   └── RouteResponse.kt          # 경로 API 응답 데이터 모델
│   ├── repository
│   │   └── LocationRepository.kt     # API 호출 및 데이터 처리 로직을 관리하는 레포지토리
├── ui
│   ├── components
│   │   ├── AppBar.kt                 # 화면 상단의 AppBar UI 컴포넌트
│   │   ├── BottomSheet.kt            # 오류를 표시하는 BottomSheet 컴포넌트
│   │   └── List.kt                   # 위치 정보를 리스트 형태로 보여주는 컴포넌트
│   ├── maps
│   │   ├── AddLabelToMap.kt          # 지도에 라벨을 추가하는 기능
│   │   └── AddRouteToMap.kt          # 지도에 경로를 추가하는 기능
│   ├── screens
│   │   ├── MainScreen.kt             # 메인 리스트 UI
│   │   └── MapScreen.kt              # 경로 및 교통 정보 표시하는 지도 화면 UI
│   └── theme
│       ├── Color.kt                  # 경로 및 전반적인 색상 정의
│       ├── Theme.kt               
│       └── Type.kt               
├── Utils
│   └── ErrorMessage.kt               # 에러 메시지 관리 유틸리티
├── viewModels
│   ├── MainState.kt                  # Mavericks 기반의 상태 관리 클래스
│   └── MainViewModel.kt              # Mavericks 기반의 ViewModel
├── MainActivity.kt                   # 메인 리스트 액티비티 (앱의 진입점)
├── MapActivity.kt                    # 경로, 시간, 거리를 보여주는 지도 액티비티
├── MyApplication.kt                  # 전역 애플리케이션 설정 및 초기화 관리
```
---
## 📌 주요 구현 사항

### 1. **상태 관리 및 비동기 데이터 처리 (Mavericks, Coroutines)**
- `MainViewModel`에서 **Mavericks** 기반으로 상태 관리 수행
  - `fetchRoutes()`, `fetchLocations()` 메서드 통해 API에서 데이터를 가져오고 상태 업데이트
- **ViewModelScope**와 **Coroutines**를 사용해 API 요청을 비동기적으로 처리
  - 메인 스레드를 차단하지 않고 UI가 계속 응답할 수 있도록 최적화
- **Mavericks 라이브러리**의 `mavericksViewModel()` 헬퍼 함수를 통해 ViewModel의 생명 주기 관리
  - **MainState 데이터 클래스**를 불변 데이터 클래스로 사용해 **상태가 변경될 때마다 새로운 상태 객체가 생성**
  - 상태 변경 시 UI가 자동으로 렌더링

### 2. **API 클라이언트 (Retrofit)**
- **Retrofit**을 사용해 네트워크 요청을 처리
- **싱글톤 패턴**을 사용해 `ApiClient`에서 **Retrofit 인스턴스**를 관리
  - 네트워크 효율성 향상
  - 유지보수성 향상

### 3. **Sealed Class를 사용한 타입 안전성**
- 경로 API 호출 결과가 두 가지 타입으로 나올 수 있으므로 **Sealed Class** 사용
- **Success**와 **Error** 상태를 명확하게 분리
  - 가독성, 유지보수성 향상
  - 호출하는 쪽에서 각 응답 타입을 쉽게 다룰 수 있도록 처리 과정을 간소화

### 4. **UI 구현 (Jetpack Compose)**
- **Jetpack Compose** 사용해 모든 UI 구현
- `Scaffold`와 **LazyColumn** 사용해 스크롤 시 필요한 항목만 동적으로 그림
  - 효율적인 레이아웃 구성
  - 메모리 사용량 줄여 성능 향상
- `MainScreen`에서 사용자가 선택한 경로가 에러 발생 시 **BottomSheet** 컴포넌트를 통해 사용자에게 에러 메시지 표시

### 5. **경로 조회 및 네비게이션**
- 사용자가 출발지와 도착지를 선택하면 **Kakao Mobility API**를 통해 경로 정보 조회
  - 성공적으로 조회된 경로는 지도에 표시되며, 사용자는 교통 정보 확인 가능
  - 경로 조회에 실패햐면 **BottomSheet**로 에러메세지와 에러 코드 확인 가능
- **Intent**를 사용해 `MapActivity`로 경로 정보를 전달 및 **Kakao Map SDK**를 통해 지도에 경로 시각화

### 6. **지도 연동 (Kakao Map SDK)**
- **Kakao Map SDK** 사용해 경로 정보와 교통 상황을 지도에 시각적으로 표시
  - **RouteLine** 사용해 세그먼트마다 각각 다른 스타일을 주어 실시간 교통 상태에 따른 색 표시 추가
  - **Label** 사용해 경로의 시작점과 끝점에 **라벨** 추가


---

## ⚙️ 기능

- **출발지 및 도착지 선택**: 사용자가 선택한 출발지와 도착지에 따라 경로 조회
- **실시간 경로 표시**: 경로 조회 결과를 실시간 교통 정보를 반영해 지도에 표시
- **거리 및 시간 정보 표시**: 경로 조회 후 이동 거리와 예상 소요 시간 표시
- **에러 처리**: API 호출 중 발생하는 에러를 **BottomSheet**를 통해 사용자에게 알림
