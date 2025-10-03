
---

## README para el **Backend Repo**

```markdown
# Expense Tracker - Backend

Backend de la aplicación **Expense Tracker**, desarrollado con **Java 17** y **Spring Boot**.  
Expone endpoints RESTful para gestionar ingresos, gastos, autenticación de usuarios y generación de reportes.

## Demo
[Link al despliegue en Render/Heroku] (pendiente)

## Repositorio Frontend
El frontend de esta aplicación está desarrollado en **React + TailwindCSS** y se encuentra aquí:  
➡️ [Expense Tracker Frontend](https://github.com/emadeveloper/expense-tracker-frontend)

## Funcionalidades
- Registro y login de usuarios con JWT.
- CRUD completo de ingresos y gastos.
- Endpoints para obtener métricas (total, máximo, mínimo).
- Persistencia con MySQL.
- Validación de datos con DTOs.
- CORS configurado para el frontend.

## Tecnologías utilizadas
- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Hibernate / JPA**
- **MySQL**
- **Maven**

## Instalación y uso
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/usuario/expense-tracker-backend.git
   cd expense-tracker-backend

2. Configurar variables de entorno en application.properties:
  spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
  spring.datasource.username=tu_usuario
  spring.datasource.password=tu_password
  jwt.secret=tu_secret_key

3. Ejecutar la aplicación:
    mvn spring-boot:run
    La API estará disponible en:
    http://localhost:8080/api

Endpoints principales:

POST /auth/login → Login

POST /auth/register → Registro

GET /incomes/my-incomes → Listar ingresos

POST /incomes/add-income → Agregar ingreso

PUT /incomes/{id} → Actualizar ingreso

DELETE /incomes/{id} → Eliminar ingreso

GET /expenses/my-expenses → Listar gastos

POST /expenses/add-expense → Agregar gasto

PUT /expenses/{id} → Actualizar gasto

DELETE /expenses/{id} → Eliminar gasto
