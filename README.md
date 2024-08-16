# 📚 IFinder API Documentation

## 📖 Introduction

Welcome to the IFinder API documentation. This document provides detailed information about the endpoints available in the IFinder API, which you can use to interact with various aspects of the IFinder system. This includes user management, authentication, media handling, searching, and more.

## Dear Instructor,
we have created a postman collection for you to test the API endpoints. You can find the collection and environment in the following link: [IFinder Postman Collection](https://drive.google.com/drive/folders/13oxcl_wrjcoO474L5tOuuf11aKIqzbJ2?usp=sharing). We suggest you import the collection and environment to your postman and start testing the endpoints with prod environment because we have deploy this api and store all the data already. Thank you for your time and consideration🙏🏻.

Also here is Admin account email: ifinder.istad@gmail.com and password: Admin@123

## 📑 Table of Contents

- [📖 Introduction](#-introduction)
- [📑 Table of Contents](#-table-of-contents)
- [⚙️ Installation](#️-installation)
- [🚀 Usage](#-usage)
- [📬 Endpoints](#-endpoints)
  - [👤 User](#-user)
  - [🔐 Auth](#-auth)
  - [🖼️ Media](#-media)
  - [🔍 Search](#-search)
  - [📂 Collection](#-collection)
  - [🎥 Video](#-video)
  - [📜 History](#-history)
  - [🗂️ Folder](#️-folder)
  - [🔖 Bookmark](#-bookmark)
  - [📝 Feedback](#-feedback)
  - [🗃️ Scrape](#-scrape)
  - [🤖 AI](#-ai)
- [📋 Examples](#-examples)
- [🛠️ Troubleshooting](#️-troubleshooting)
- [👥 Contributors](#-contributors)

## ⚙️ Installation

To use the IFinder API, you need to have an active instance of the IFinder service running. Ensure you have the tokens for authentication. You can use tools like Postman to test the API endpoints.

## 🚀 Usage

The IFinder API uses RESTful principles and supports standard HTTP methods such as GET, POST, PUT, PATCH, and DELETE. All endpoints require the base URL `https://ifinder-api.istad.co/api/v1/`, which should be replaced with the actual base URL of your IFinder instance. We will provide you with the Postman Collection and Environment to get started. [IFinder Postman Collection](https://red-space-973450.postman.co/workspace/iFinder-Search-Engine~8db9cd82-49bc-4d36-b73c-d17813c5658c/collection/28112071-01d166e1-6978-4835-8f58-49765cd73d2c?action=share&source=collection_link&creator=28112071)

## 📬 Endpoints

### 👤 User

| Endpoint          | Method | URL                                     | Description |
|-------------------|--------|-----------------------------------------|-------------|
| Find User by UUID | GET    | `users/{{userUuid}}`                    | Retrieves user information based on the provided UUID. |
| Find All Users    | GET    | `users/all?page=0&size=10`              | Retrieves a list of all users. Supports pagination with `page` and `size` query parameters. |
| Find All Admins   | GET    | `users/all/admins`                      | Retrieves a list of all admins. |
| Update User       | PATCH  | `users/{{userUuid}}`                    | Updates user information based on the provided UUID. |
| Block User        | PUT    | `users/{{userUuid}}/block`              | Blocks a user based on the provided UUID. |
| Unblock User      | PUT    | `users/{{userUuid}}/unblock`            | Unblocks a user based on the provided UUID. |
| Delete User       | PUT    | `users/{{userUuid}}/delete`             | Soft deletes a user based on the provided UUID. |
| Find User Profile | GET    | `users/me`                              | Retrieves the authenticated user's profile information. |

### 🔐 Auth

| Endpoint              | Method | URL                                      | Description |
|-----------------------|--------|------------------------------------------|-------------|
| Register              | POST   | `auth/register`                          | Registers a new user. |
| Verify Email          | POST   | `auth/verify`                            | Verifies a user's email address using a verification code. |
| Login                 | POST   | `auth/user/login`                        | Authenticates a user and returns a JWT token. |
| Admin Login           | POST   | `auth/admin/login`                       | Authenticates an admin user and returns a JWT token. |
| Refresh Token         | POST   | `auth/refresh-token`                     | Refreshes the authentication token using a refresh token. |
| Change Password       | POST   | `auth/change-password`                   | Changes the user's password. |
| Send Verification Code| POST   | `auth/{email}/send-verify-code`          | Sends a verification code to the specified email address. |
| Reset Password Token  | POST   | `auth/reset-token`                       | Generates a reset token for password reset. |
| Reset Password        | POST   | `auth/reset-password`                    | Resets the user's password using the reset token. |
| Resend Verification Code | POST| `auth/{email}/resend-verification-code`  | Resends the verification code to the specified email address. |
| Total Record          | GET    | `auth/total-record`                      | Retrieves the total number of records. |

### 🖼️ Media

| Endpoint     | Method | URL                                    | Description |
|--------------|--------|----------------------------------------|-------------|
| Upload Image | POST   | `media/upload-image`                   | Uploads an image file. |

### 🔍 Search

| Endpoint     | Method | URL                                      | Description |
|--------------|--------|------------------------------------------|-------------|
| Search       | GET    | `search?q={query}&page=0&size=10`        | Searches the database based on the query parameter. |

### 📂 Collection

| Endpoint              | Method | URL                                          | Description |
|-----------------------|--------|----------------------------------------------|-------------|
| Find Collection by Name | GET  | `collection?name={name}`                     | Retrieves a collection based on the name query parameter. |
| Find All Collections | GET    | `collection/all?page={page}&size={size}`     | Retrieves all collections with pagination. |
| Delete Collection    | DELETE | `collection/{uuid}/delete`                   | Deletes a collection based on the provided UUID. |
| Delete All Collections | DELETE| `collection/deleteAll`                       | Deletes all collections. |
| Import Data to Collection | POST| `web-scraper/dataImport`                    | Imports data from a specified URL into a collection. |

### 🎥 Video

| Endpoint     | Method | URL                                                | Description |
|--------------|--------|----------------------------------------------------|-------------|
| Search Video | GET    | `video?page={page}&size={size}&search={searchTerm}`| Searches for videos based on the search term with pagination. |

### 📜 History

| Endpoint     | Method | URL                                                   | Description |
|--------------|--------|-------------------------------------------------------|-------------|
| Add History  | POST   | `user-history`                                        | Adds a URL to the user's history. |
| Find All History | GET| `user-history?uuid={userUuid}&page={page}&size={size}&sortOrder={order}` | Retrieves the user's history with pagination and sort order. |
| Delete History | DELETE| `user-history`                                       | Deletes a history record based on the provided UUID. |

### 🗂️ Folder

| Endpoint            | Method | URL                                                   | Description                                       |
|---------------------|--------|-------------------------------------------------------|---------------------------------------------------|
| Create Folder       | POST   | `folders`                                             | Creates a new folder.                             |
| Delete Folder       | DELETE | `folders`                                             | Deletes a folder based on the provided UUID.      |
| Find Folder by UUID | GET    | `folders/{uuid}`                                      | Retrieves a folder based on the provided UUID.    |
| Find All Folders    | GET    | `folders?userUuid={userUuid}&page={page}&size={size}` | Retrieves all folders for a user with pagination. |
| Update Folder Name  | PUT    | `folders`                                             | Updates the name of a folder.                     |
| Download Folder     | POST   | `folders/download?userUuid={{userUuid}}`              | Download User folder                              |
| Upload Folder       | POST   | `folders/upload/{{userUuid}}`                         | Upload folder to User account                     |

### 🔖 Bookmark

| Endpoint            | Method | URL                                                        | Description |
|---------------------|--------|------------------------------------------------------------|-------------|
| Create Bookmark     | POST   | `bookmarks`                                                | Creates a new bookmark. |
| Find All Bookmarks  | GET    | `bookmarks?userUuid={userUuid}&page={page}&size={size}`    | Retrieves all bookmarks for a user with pagination. |
| Delete Bookmark     | DELETE | `bookmarks`                                                | Deletes a bookmark based on the provided UUID. |
| Update Bookmark     | PUT    | `bookmarks/update`                                         | Updates a bookmark title. |
| Find Bookmark by UUID | GET  | `bookmarks/{uuid}`                                         | Retrieves a bookmark based on the provided UUID. |

### 📝 Feedback

| Endpoint           | Method | URL                                                  | Description |
|--------------------|--------|------------------------------------------------------|-------------|
| Create Feedback    | POST   | `feedback`                                           | Submits feedback. |
| Find All Feedback  | GET    | `feedback?page={page}&size={size}`                   | Retrieves all feedback with pagination. |
| Delete Feedback    | DELETE | `feedback/{uuid}/delete`                             | Deletes feedback based on the provided UUID. |

### 🗃️ Scrape

| Endpoint               | Method | URL                                                  | Description |
|------------------------|--------|------------------------------------------------------|-------------|
| Scrape ExpressJS       | GET    | `http://34.143.180.228:3000/scrape?url={url}`         | Scrapes data from the specified URL using ExpressJS. |
| Scrape Data            | POST   | `web-scraper/scrape`                                 | Scrapes data from the specified URL. |
| Import Data            | POST   | `web-scraper/dataImport`                             | Imports data from a specified URL. |
| Change Scrape Category | POST   | `web-scraper/changeCategory`                         | Changes the scrape category. |
| Auto Scrape Action     | POST   | `web-scraper/autoScrapeAction`                       | Starts or stops auto-scraping. |

### 🤖 AI

| Endpoint | Method | URL                                         | Description |
|----------|--------|---------------------------------------------|-------------|
| AI Search| GET    | `gemini/search?prompt={prompt}`             | Performs an AI-based search using the given prompt. |

## 📋 Examples

For detailed examples on how to use the API endpoints, please refer to the provided Postman collection link: [IFinder Postman Collection](https://red-space-973450.postman.co/workspace/iFinder-Search-Engine~8db9cd82-49bc-4d36-b73c-d17813c5658c/collection/28112071-01d166e1-6978-4835-8f58-49765cd73d2c?action=share&source=collection_link&creator=28112071)

## 🛠️ Troubleshooting

If you encounter issues while using the IFinder API, consider the following steps:
- Ensure your request URLs are correct and the base URL is properly set.
- Check your tokens for validity and expiration.
- Verify the request body and headers match the expected format.
- Refer to the response messages for specific error details.

## 👥 Contributors

- IFinder Team
- ISTAD Institute of Science and Technology Advanced Development
