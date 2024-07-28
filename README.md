# Spring Boot Web MVC with Microsoft Azure Active Directory Integration

This repository demonstrates how to build a Spring Boot Web MVC application that provides API endpoints integrated with **Microsoft Azure Active Directory** (AAD) for managing organizational identities. The application implements the "**Resource Owner Password Credentials** (ROPC) Authentication" scheme in Active Directory, leveraging **Microsoft Graph APIs**. This scheme is suitable for scenarios where a custom registration and authentication process with a tailored UI/UX is a business requirement for your organization.

## Features

- **Spring Boot Web MVC**: Leveraging Spring Boot for rapid application development with RESTful endpoints.
- **Microsoft Azure Active Directory Integration**: Secure API endpoints using Azure AD for authentication and authorization.
- **Resource Owner Password Credentials (ROPC) Authentication**: Implementing ROPC flow for custom user registration and authentication.

## Prerequisites

- Java 21 or higher
- Gradle
- Azure Active Directory tenant
- Azure AD application registration with appropriate permissions

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/misoft-pro/demo-spring-azure-ad-integration.git
cd demo-spring-azure-ad-integration
```

### Configuration

1. **Azure AD Configuration**:
    - Register a new application in the Azure portal.
    - Note down the Tenant ID, Client ID, and Client Secret.
    - Configure API permissions and expose an API if required.

2. **Application Properties**:
    - Update the `application.yml` with your Azure AD credentials:

```yaml
azure:
  active-directory:
    tenant-id: YOUR_TENANT_ID
    client-id: YOUR_CLIENT_ID
    client-secret: YOUR_CLIENT_SECRET
    user-group:
      allowed-groups: GROUP_ID_1, GROUP_ID_2
```

### Build and Run the Application

#### Using Gradle

```sh
./gradlew clean build
./gradlew bootRun
```

## API Endpoints

### Authentication

- **Login**: `POST /api/v1/tokens`
    - Request Body: `{ "username": "user@example.com", "password": "yourpassword" }`
    - Response: `{ "token": "JWT_TOKEN" }`

### User Management

- **Create User**: `POST /api/v1/users`
    - Request Body: `{ "username": "user@example.com", "password": "yourpassword"}`
    - Response: `{ "id": "USER_ID" }`

- **Get User**: `GET /api/users/{userId}`
-   - Request Header: `{ "Authorization": "Bearer yourJwtToken"}`
    - Response: `{ "id": "USER_ID" }`

- **Patch User**: `PATCH /api/v1/users/{userId}`
    - Request Body: `{ "fieldToUpdate": "newValue" }`
    - Request Header: `{ "Authorization": "Bearer yourJwtToken"}`
    - Response: `{ "id": "USER_ID", "fieldToUpdate": "newValue" }`

## Security

The application uses JWT (JSON Web Token) for securing API endpoints. The tokens are issued by Azure AD and are verified by the application before granting access to protected resources.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss improvements or fixes.

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Microsoft Azure Active Directory](https://azure.microsoft.com/en-us/services/active-directory/)
