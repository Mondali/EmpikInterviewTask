# Backend Recruitment Task - User Information Service

## Change Log

### v1.0.1
- Changed user calculation precision

### v1.0.0
- Initial version of the service.

## Author
Paweł Waśkiewicz

## Overview

This RESTful service retrieves user information from the GitHub API and extends it with custom calculations. It is designed for a recruitment task and demonstrates handling API requests, performing calculations, and managing a database for request counts.

## Features

- Retrieves user information by GitHub username.
- Extends GitHub user data with custom calculations.
- Tracks the number of API requests per user in a database.


## API Endpoints

### GET /users/{login}

Fetches user information based on their GitHub username and adds custom calculations.

**Response:**

```json
{
  "id": "user_id",
  "login": "user_login",
  "name": "user_name",
  "type": "user_type",
  "avatarUrl": "url_to_avatar",
  "createdAt": "account_creation_date",
  "calculations": "result_of_custom_calculations"
}
```

## Stack

- Java 17
- Spring Boot 3.3.1
- H2 Database
- Testcontainers