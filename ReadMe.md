# Kakao Mobility Test Application

## 프로젝트 개요

이 프로젝트는 사용자가 출발지와 도착지를 선택하여 경로를 조회하고, 해당 경로에 대한 교통 정보를 지도에 표시하는 Android 애플리케이션입니다. **Kakao Mobility API**를 사용하여 경로 정보를 제공하고, **Jetpack Compose**를 사용한 UI와 **Mavericks**를 활용한 상태 관리를 구현했습니다.

---

## 기술 스택

- **언어**: Kotlin
- **UI 프레임워크**: Jetpack Compose
- **상태 관리**: Mavericks (ViewModel 및 상태 관리)
- **네트워크**: Retrofit
- **지도 API**: Kakao Map SDK
- **의존성 관리**: Gradle

---

## ApiClient에서 Retrofit을 싱글톤으로 제공

## 목록을 보여줄 때 LazyColumn 사용해 화면에 표시되는 항목만 렌더링해 성능을 최적화
- 스크롤할 때 필요한 항목을 동적으로 그려줌
- 메모리 사용량을 줄이고 성능 향상
- 대용량 데이터 처리 시 유용

## sealed class 사용
- API 호출 결과가 여러 가지 다른 타입으로 나오는 경우에도 하나의 공통된 타입으로 처리
- 타입 안정성 보장, 타입별 적절한 처리
- 호출하는 쪽에서 각 응답 타입을 다루지 않고 데이터 클래스 내에서 다루어 과정 간소화


## BottomSheet 사용


## Mavericks 라이브러리를 사용해 mavericksViewModel() 헬퍼 함수로 ViewModel의 생명 주기 관리
- MainState 데이터 클래스를 불변 데이터 클래스로 사용해 setState에서 상태가 변경될 때마다 새로운 상태 객체가 생성
- 상태 변경 시 UI 자동 렌더링

## 코루틴 사용
- API 요청을 비동기적으로 처리
- 메인 스레드가 차단되지 않고 UI가 계속 응답할 수 있도록 코루틴 사용