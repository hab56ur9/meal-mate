# 서비스 소개 
식단 관리 모바일 어플리케이션 
# 기획 의도 
- 건강에 대한 관심 증가에 따른 맞춤형 식단 관리 수요 증가
- 90%의 사람이 특정 영양 성분에 주의를 기울이고, 56.8%의 사람이 특별한 식단을 실천중
- 기존 식단 관리 앱은 체중 감량이나 증량 중 한 가지에만 초점을 두고 있어 사용자의 다양한 식단 목표를 만족시키지 못하는 문제점

# 기능 소개
- 사용자의 식단 목표와 선호도를 고려한 맞춤 식단 관리 앱 개발
- 사용자의 주요 활동량, 성별, 연령 등 개별적 특성을 고려한 권장 칼로리 제공
- 음식의 칼로리 및 주요 영양소 정보 제공하여 사용자가 식단을 스스로 구성하고 관리할 수 있게 함

# 시연 영상
[![밀메이트 시연영상](http://img.youtube.com/vi/en6vzEMh3GY/0.jpg)](https://youtu.be/en6vzEMh3GY?t=0s) 
### 이미지를 클릭하면 유튜브로 이동합니다.

# UI 및 포스터
![Screenshot 2024-11-24 at 20 53 43](https://github.com/user-attachments/assets/a9476223-8ef2-4008-978b-4b27c40fb9e1)
![Screenshot 2024-11-24 at 20 53 48](https://github.com/user-attachments/assets/28832a33-7bb4-4402-9fc7-bdc443db1621)

[밀메이트_포스터.pdf](https://github.com/user-attachments/files/17893080/_.pdf)

# 수상
## 학술대회 참가
 - 2023 한국 컴퓨터 정보 학회 하계 학술대회 참가
 - [68차 31권 2호 논문집에 포함](https://github.com/user-attachments/files/19274109/2023.68.31.2.pdf)
## 캡스톤 디자인 경진대회
 - 장려상
 - 명지전문대 주최

# version
## 2023.03~2023.06  v1.0 식단 관리 기능 구현
- adapter view 중첩 사용으로 식단 및 음식 설정 기능 구현
- 현재 체중 및 키, 성별등 데이터를 바탕으로 사용자의 필요 칼로리 계산
- 달력 기능 추가 및 식단 확인 가능
- SQLite를 이용한 로컬 저장 기능 구현
- MVVM 디자인 패턴 적용
- ExecuterService를 이용하여 모든 요청 비동기 처리
- State패턴과 Observer, Singleton 패턴을 결합하여 전역 상태관리 모듈 개발


# 프로젝트 회고 
### 다양한 디자인 패턴 적용 경험
Singleton, builder, State, Observer 등 GoF의 다양한 디자인 패턴을 적용시켜보았다. 디자인 패턴 적용시 수정과 확장이 용이함을 느끼게 되었다.
### ExecuterService를 이용한 Thread 풀 관리 및 비동기 처리 
반응성 향상을 위해 사용자의 입력시 비동기 처리로 동작하게 처리했다. 비동기처리시 UI 업데이트 트리거가 제대로 작동하지 않는 문제가 생겼는데 이를 Observer를 통해 UI가 업데이트 된 시점을 트리거로 새로고침 로직을 구현해 해결했다. 
### 중첩 Adapter의 의존성 처리 
adapter중첩 사용으로 생성시 의존성 코드가 엄청 복잡해지는 문제가 발생했다. State 패턴과 Observer , Singleton을 결합해 전역 상태 관리 모듈을 제작하고 databind library를 활용하여 xml DI가 가능하게 했다. 이를 바탕으로 adapter에 필요한 의존성을 xml에서 처리하는 방식으로 의존성 코드가 대량으로 줄어들었다.


