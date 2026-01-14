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

mvp+repository
model/
  Order, OrderStatus
repository/
  OrderRepository (interface)
  FileOrderRepository (CSV/binar)
presenter/
  OrdersPresenter (Controller)
service/
  OrderService (logica de business + notificare)
view/
  thymeleaf templates
util/
  NotificationUtil
Roluri

Model: doar date + reguli simple (ex: canTransitionTo)

Repository: citește/scrie fișierul

Service: aplică tranziția + persistă + notifică

Presenter: ia input din UI, cheamă service, pune date în model (Thymeleaf)


