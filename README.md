mpai-mvp-active-record/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/mpai/
│  │  │  ├─ MpaiApplication.java
│  │  │  ├─ presenter/
│  │  │  │  ├─ HomePresenter.java
│  │  │  │  └─ OrdersPresenter.java
│  │  │  ├─ model/
│  │  │  │  ├─ Order.java
│  │  │  │  └─ OrderStatus.java
│  │  │  └─ util/
│  │  │     └─ NotificationUtil.java
│  │  ├─ resources/
│  │  │  ├─ application.yml
│  │  │  └─ templates/
│  │  │     ├─ home.html
│  │  │     ├─ orders.html
│  │  │     └─ order-details.html
├─ data/
│  └─ orders.csv   (se creează automat dacă nu există)
└─ README.md
