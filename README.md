# Board-Project
![boardDemo](https://user-images.githubusercontent.com/54926902/149363115-3f49d826-d652-4882-8101-055d728eec0e.gif)

# 1. DB 생성
mysql -u root -p < database/schema.sql

# 2. 백엔드 (8080)
cd backend && ./mvnw spring-boot:run

# 3. 프론트엔드 (5173)
cd frontend && npm install && npm run dev