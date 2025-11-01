# Elfagr - E-Commerce & Inventory Management System

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Domain Models](#domain-models)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Configuration](#configuration)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)

---

## 🎯 Overview

**Elfagr** is a comprehensive E-Commerce and Inventory Management System built with Spring Boot. It provides a full-featured backend solution for managing products, inventory, orders, returns, users, and transactions. The system supports multiple inventory locations, role-based access control, email verification, and comprehensive order management with various payment methods.

### Key Capabilities

- **Product Management**: Create, update, and manage products with categories, SKUs, barcodes, and images
- **Inventory Management**: Multi-location inventory tracking with real-time stock updates
- **Order Processing**: Complete order lifecycle management (Pending → Processed → Shipped → Delivered)
- **Return Management**: Handle product returns with reason tracking
- **User Management**: Role-based access control (Admin, Employee, User) with email verification
- **Transaction Tracking**: Comprehensive inventory transaction history
- **Image Upload**: Integration with ImgBB for product image storage
- **Email Service**: Automated email verification and notifications

---

## ✨ Features

### Authentication & Authorization
- JWT-based authentication
- Email verification on registration
- Password reset functionality
- Role-based access control (RBAC)
- Three user roles: ADMIN, EMPLOYEE, USER

### Product Management
- Product CRUD operations
- Product categorization
- SKU and barcode support
- Product status management (NEW, IMPORTED, AVAILABLE, UNAVAILABLE)
- Image upload and management
- Product search by name, SKU, barcode, and category

### Inventory Management
- Multiple inventory locations
- Real-time stock tracking
- Product-inventory relationships
- Inventory transaction history
- Transaction types: IN, OUT
- Transaction reasons tracking

### Order Management
- Complete order lifecycle
- Multiple payment methods (Cash, Credit Card, E-Wallet)
- Order status tracking (Pending, Processed, Shipped, Delivered, Canceled)
- Customer information tracking
- Order filtering and search capabilities

### Return Management
- Return request processing
- Return reason tracking
- Return item management
- Integration with order system

### Additional Features
- Pagination and sorting for all list endpoints
- Soft delete functionality
- Comprehensive error handling
- OpenAPI/Swagger documentation
- Redis caching support
- Asynchronous email processing
- CORS configuration
- SSL/TLS support

---

## 🛠 Technology Stack

### Backend Framework
- **Spring Boot 3.2.5**
- **Java 17**

### Core Dependencies
- **Spring Data JPA** - Database persistence
- **Spring Security** - Authentication and authorization
- **Spring Web** - RESTful API development
- **Spring Mail** - Email service
- **Spring Cache** - Caching support

### Database
- **MySQL 8.0** - Primary relational database
- **Redis** - Caching layer

### Authentication & Security
- **JWT (jjwt 0.11.5)** - Token-based authentication
- **BCrypt** - Password encryption

### API Documentation
- **SpringDoc OpenAPI 3** - API documentation (Swagger UI)

### Caching
- **Caffeine** - Local cache provider
- **Redis** - Distributed cache

### Image Handling
- **ImgBB API** - Image upload and storage
- **Multipart File Support** - Up to 50MB file size

### Development Tools
- **Lombok** - Reduced boilerplate code
- **Maven** - Dependency management
- **Spring Boot DevTools** - Development utilities

### HTTP Client
- **Apache HttpClient 5** - HTTP requests

---

## 🏗 Architecture

The project follows a **layered architecture** pattern:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (Domain Models)
    ↓
Database (MySQL)
```

### Key Design Patterns

1. **DTO Pattern**: Data Transfer Objects for API communication
2. **Mapper Pattern**: Conversion between Entities and DTOs
3. **Repository Pattern**: Data access abstraction
4. **Service Layer Pattern**: Business logic encapsulation
5. **Filter Pattern**: JWT authentication filter
6. **Builder Pattern**: Entity and DTO construction (via Lombok)

---

## 📊 Domain Models

### User
- **Fields**: id, name, email, password, phone, birthdate, gender, role, status, address, imageUrl
- **Roles**: ADMIN, EMPLOYEE, USER
- **Status**: ACTIVE, INACTIVE, SUSPENDED
- **Relationships**: 
  - One-to-Many with Orders
  - One-to-Many with Returns
  - One-to-Many with InventoryTransactions

### Product
- **Fields**: id, name, sku, barcode, description, price, imageUrl, productStatus
- **Status**: NEW, IMPORTED
- **Availability**: Available/Unavailable flag
- **Relationships**:
  - Many-to-One with Category
  - One-to-Many with ProductInventory
  - One-to-Many with OrderItems
  - One-to-Many with ReturnItems

### Category
- **Fields**: id, name, description, status
- **Status**: ACTIVE, INACTIVE

### Inventory
- **Fields**: id, name, description, address, contactInfo, type, status
- **Type**: WAREHOUSE, STORE, DISTRIBUTION_CENTER
- **Status**: ACTIVE, INACTIVE
- **Relationships**:
  - One-to-Many with ProductInventory
  - One-to-Many with InventoryTransactions
  - One-to-Many with OrderItems
  - One-to-Many with ReturnItems

### ProductInventory
- **Fields**: id, quantity, isAvailable
- **Relationships**:
  - Many-to-One with Product
  - Many-to-One with Inventory

### Order
- **Fields**: id, customerName, customerPhone, customerInfo, totalPrice, taxAmount, discountAmount, orderStatus, paymentMethod, notes
- **Status**: PENDING, PROCESSED, SHIPPED, DELIVERED, CANCELED
- **Payment Methods**: CASH, CREDIT_CARD, E_WALLET
- **Relationships**:
  - Many-to-One with User
  - One-to-Many with OrderItems
  - One-to-One with Return

### OrderItem
- **Fields**: id, quantity, unitPrice, subTotal
- **Relationships**:
  - Many-to-One with Order
  - Many-to-One with Product
  - Many-to-One with Inventory

### InventoryTransaction
- **Fields**: id, type, reason, quantityChange, createdAt
- **Types**: IN, OUT
- **Reasons**: SALE, PURCHASE, RETURN, ADJUSTMENT, DAMAGED, EXPIRED
- **Relationships**:
  - Many-to-One with Inventory
  - Many-to-One with Product
  - Many-to-One with User

### Return
- **Fields**: id, customerName, customerPhone, customerInfo, totalAmount, reason, notes
- **Reasons**: DEFECTIVE, WRONG_ITEM, CUSTOMER_REQUEST, DAMAGED_IN_TRANSIT
- **Relationships**:
  - One-to-One with Order
  - Many-to-One with User
  - One-to-Many with ReturnItems

### ReturnItem
- **Fields**: id, quantity, unitPrice, subTotal
- **Relationships**:
  - Many-to-One with Return
  - Many-to-One with Product
  - Many-to-One with Inventory

---

## 🔌 API Endpoints

### Authentication (`/api/auth`)
- `POST /api/auth/register` - User registration
- `POST /api/auth/verify-account` - Email verification
- `POST /api/auth/login` - User login
- `POST /api/auth/reset-password` - Password reset
- `POST /api/auth/resend-code` - Resend verification code
- `GET /api/auth/say-hello` - Health check

### Products (`/api/products`)
- `GET /api/products/` - Get all products (paginated)
- `GET /api/products/product/{id}` - Get product by ID
- `GET /api/products/product/name` - Search products by name
- `GET /api/products/category/{id}` - Get products by category
- `GET /api/products/category/name` - Get products by category name
- `GET /api/products/inventory/{id}/new-product/sku` - Get new product by SKU
- `GET /api/products/inventory/{id}/new-product/barcode` - Get new product by barcode
- `GET /api/products/inventory/{id}/imported-product/sku` - Get imported product by SKU
- `GET /api/products/inventory/{id}/imported-product/barcode` - Get imported product by barcode
- `POST /api/products/product` - Create product
- `PUT /api/products/product/{id}` - Update product
- `PATCH /api/products/product/{productId}/category/{categoryId}` - Change product category
- `PATCH /api/products/available/product/{id}` - Mark product as available
- `PATCH /api/products/un-available/product/{id}` - Mark product as unavailable
- `PATCH /api/products/new/product/{id}` - Mark product as new
- `PATCH /api/products/imported/product/{id}` - Mark product as imported
- `DELETE /api/products/product/{id}` - Soft delete product

### Categories (`/api/categories`)
- See CategoryController for all endpoints

### Product Inventories (`/api/product-inventories`)
- See ProductInventoryController for all endpoints

### Orders (`/api/orders`)
- `GET /api/orders/` - Get all orders (paginated)
- `GET /api/orders/order/{id}` - Get order by ID
- `GET /api/orders/customerName` - Get orders by customer name
- `GET /api/orders/customerPhone` - Get orders by customer phone
- `GET /api/orders/date` - Get orders by creation date
- `GET /api/orders/employee/{id}` - Get orders by employee
- `GET /api/orders/pending` - Get pending orders
- `GET /api/orders/processed` - Get processed orders
- `GET /api/orders/shipped` - Get shipped orders
- `GET /api/orders/delivered` - Get delivered orders
- `GET /api/orders/canceled` - Get canceled orders
- `GET /api/orders/cash` - Get cash payments
- `GET /api/orders/e-wallet` - Get e-wallet payments
- `GET /api/orders/credit-card` - Get credit card payments
- `POST /api/orders/order` - Create order
- `PATCH /api/orders/pending/order/{id}` - Set order as pending
- `PATCH /api/orders/processed/order/{id}` - Set order as processed
- `PATCH /api/orders/shipped/order/{id}` - Set order as shipped
- `PATCH /api/orders/delivered/order/{id}` - Set order as delivered
- `PATCH /api/orders/canceled/order/{id}` - Set order as canceled

### Returns (`/api/returns`)
- See ReturnController for all endpoints

### Inventories (`/api/inventories`)
- See InventoryController for all endpoints

### Inventory Transactions (`/api/inventory-transactions`)
- See InventoryTransactionController for all endpoints

### Users (`/api/users`)
- See UserController for all endpoints

---

## 🔒 Security

### Authentication Flow

1. User registers with email, password, and other details
2. System sends verification code to email
3. User verifies email with code
4. User logs in and receives JWT token
5. Token is included in subsequent requests (Bearer token)

### JWT Configuration
- **Algorithm**: HMAC SHA-512
- **Expiration**: 86400000ms (24 hours)
- **Secret Key**: Configured in `application.properties`

### Security Features
- Password encryption using BCrypt
- JWT token-based authentication
- Role-based authorization (`@PreAuthorize`)
- CORS configuration
- Content Security Policy (CSP) headers
- Stateless session management
- Custom authentication entry point

### Protected Endpoints
All endpoints except `/api/auth/**` and Swagger UI require authentication.

### Role-Based Access
- **ADMIN**: Full access to all operations
- **EMPLOYEE**: Access to operational endpoints (products, orders, inventory)
- **USER**: Limited access (future implementation)

---

## ⚙️ Configuration

### Application Properties

The main configuration is in `src/main/resources/application.properties`:

#### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elfagr
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

#### Server Configuration
```properties
server.port=8090
server.ssl.key-store=classpath:localCertificate.pfx
server.ssl.key-store-type=PKCS12
```

#### Mail Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

#### JWT Configuration
```properties
spring.jwt.secretKey=your-secret-key
spring.jwt.expiration=86400000
```

#### Image Upload Configuration
```properties
imgbb.api.key=your-imgbb-api-key
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

#### Redis Configuration
```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.cache.type=redis
```

#### CORS Configuration
```properties
app.cors.allowed-origins=https://localhost:3000
```

---

## 📋 Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 17** or higher
2. **Maven 3.6+** (or use the included Maven Wrapper)
3. **MySQL 8.0+** database server
4. **Redis Server** (for caching)
5. **ImgBB API Key** (for image uploads)
6. **SSL Certificate** (optional, for HTTPS)
7. **Gmail App Password** (for email service)

---

## 🚀 Setup & Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Elfagr
```

### 2. Database Setup

Create a MySQL database:
```sql
CREATE DATABASE elfagr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Redis Setup

Ensure Redis is running on `localhost:6379` (or update configuration):
```bash
redis-server
```

### 4. Configuration

Update `src/main/resources/application.properties` with your settings:
- Database credentials
- Email credentials
- ImgBB API key
- JWT secret key
- SSL certificate (if using HTTPS)

### 5. Build the Project

Using Maven Wrapper (Windows):
```bash
mvnw.cmd clean install
```

Using Maven Wrapper (Linux/Mac):
```bash
./mvnw clean install
```

Or using Maven directly:
```bash
mvn clean install
```

### 6. Run the Application

Using Maven Wrapper:
```bash
mvnw.cmd spring-boot:run
```

Or directly:
```bash
mvn spring-boot:run
```

### 7. Access the Application

- **API Base URL**: `https://localhost:8090` (or `http://localhost:8090` if SSL is disabled)
- **Swagger UI**: `http://localhost:8090/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8090/v3/api-docs`

---

## 📐 Database Schema

The application uses JPA/Hibernate with automatic schema generation (`ddl-auto=update`). Key tables include:

- `users` - User accounts and authentication
- `products` - Product catalog
- `categories` - Product categories
- `inventories` - Inventory locations
- `productInventory` - Product stock per location
- `orders` - Customer orders
- `order_items` - Order line items
- `returns` - Return requests
- `return_items` - Return line items
- `inventoryTransactions` - Inventory transaction history

All tables support soft deletes via `isDeleted` flags and timestamps (`createdAt`, `updatedAt`, `deletedAt`).

---

## 📁 Project Structure

```
Elfagr/
├── src/
│   ├── main/
│   │   ├── java/com/example/Elfagr/
│   │   │   ├── Configuration/          # Spring configuration classes
│   │   │   │   ├── AsyncConfig.java
│   │   │   │   ├── CacheConfig.java
│   │   │   │   ├── JacksonConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── RestTemplateConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── Exception/              # Exception handling
│   │   │   │   ├── CustomAuthenticationEntryPoint.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ImageUploadException.java
│   │   │   ├── Inventory/              # Inventory module
│   │   │   │   ├── Controller/
│   │   │   │   ├── DTO/
│   │   │   │   ├── Entity/
│   │   │   │   ├── Enum/
│   │   │   │   ├── Mapper/
│   │   │   │   ├── Repository/
│   │   │   │   └── Service/
│   │   │   ├── Order/                  # Order module
│   │   │   ├── Product/                # Product module
│   │   │   ├── Return/                 # Return module
│   │   │   ├── Security/               # Authentication & Security
│   │   │   ├── User/                   # User module
│   │   │   ├── Mail/                   # Email service
│   │   │   └── Shared/                 # Shared utilities
│   │   └── resources/
│   │       ├── application.properties
│   │       └── banner.txt
│   └── test/                           # Test files
├── pom.xml                             # Maven configuration
├── mvnw                                 # Maven wrapper (Unix)
└── mvnw.cmd                            # Maven wrapper (Windows)
```

---

## 💻 Usage

### Authentication Flow Example

1. **Register a new user**:
```bash
POST /api/auth/register
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "1234567890",
  "birthdate": "1990-01-01",
  "gender": "MALE",
  "role": "EMPLOYEE",
  "address": "123 Main St"
}
```

2. **Verify email**:
```bash
POST /api/auth/verify-account
{
  "email": "john@example.com",
  "code": "123456"
}
```

3. **Login**:
```bash
POST /api/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```

4. **Use JWT token in subsequent requests**:
```bash
GET /api/products/
Authorization: Bearer <your-jwt-token>
```

### Creating a Product Example

```bash
POST /api/products/product
Authorization: Bearer <token>
Content-Type: multipart/form-data

{
  "name": "Laptop",
  "sku": "LAP-001",
  "barcode": "1234567890123",
  "description": "High-performance laptop",
  "price": 999.99,
  "productStatus": "NEW",
  "categoryId": 1
}
+ image file
```

### Creating an Order Example

```bash
POST /api/orders/order
Authorization: Bearer <token>
{
  "customerName": "Jane Smith",
  "customerPhone": "0987654321",
  "orderStatus": "PENDING",
  "paymentMethod": "CASH",
  "orderItems": [
    {
      "productId": 1,
      "inventoryId": 1,
      "quantity": 2,
      "unitPrice": 999.99
    }
  ]
}
```

---

## 📚 API Documentation

The application includes interactive API documentation via Swagger UI:

- **Swagger UI**: `http://localhost:8090/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8090/v3/api-docs`

The documentation includes:
- All available endpoints
- Request/response schemas
- Authentication requirements
- Try-it-out functionality

To access protected endpoints in Swagger UI:
1. Click the "Authorize" button
2. Enter your JWT token (format: `Bearer <token>`)
3. Click "Authorize"

---

## 🔧 Development

### Running Tests
```bash
mvn test
```

### Code Formatting
The project uses Lombok to reduce boilerplate code. Ensure your IDE has Lombok plugin installed.

### Hot Reload
Spring Boot DevTools is included for automatic application restart during development.

---

## 📝 Additional Notes

### Soft Delete Pattern
Most entities support soft deletion through `isDeleted` flags rather than physical deletion from the database.

### Pagination
List endpoints support pagination with parameters:
- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: createdAt)
- `direction` (asc/desc)

### Image Upload
Images are uploaded to ImgBB and the URL is stored in the database. Maximum file size is 50MB.

### Email Service
The system uses Gmail SMTP for sending verification codes and notifications. Ensure you use an App Password, not your regular Gmail password.

### Caching
Redis is used for caching. Ensure Redis is running before starting the application. Cache can be disabled by commenting out cache configuration in `application.properties`.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is a demo/educational project. Please check with the repository owner for license information.

---

## 👥 Authors

- **Elfagr Development Team**

---

## 🙏 Acknowledgments

- Spring Boot community
- All open-source contributors

---

## 📞 Support

For issues, questions, or contributions, please open an issue in the repository.

---

**Last Updated**: 2024
**Version**: 1.0.0
