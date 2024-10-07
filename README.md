# Brokage Firm API

Brokage Firm API is a backend API service designed for brokage firms, 
enabling their employees to manage stock orders and financial transactions for customers. 

The API provides endpoints for creating, listing, and canceling orders, as well as handling deposit and withdrawal operations.

The system ensures proper validation of assets and balances when placing or canceling orders, while integrating flexible filters for more efficient order and asset management.

---

# Goals

**✅ Order Management**

- Enable employees to easily create, list, and cancel stock orders for customers.
- Ensure that only PENDING orders can be canceled, while orders with other statuses remain immutable.

**✅ Secure Money Management**

- Allow employees to deposit and withdraw funds (TRY) for customers, ensuring proper balance management.
- Implement IBAN verification for secure withdrawals.

**✅ Asset Validation**

- Ensure customers have sufficient usable TRY assets before creating new orders.
- Automatically update usable asset balances when orders are created or canceled.

**✅ Authorization & Security**

- All endpoints require admin authentication with a username and password to ensure secure access.

**✅ Comprehensive API Coverage**

- Include filters for listing orders and assets, making it easier for employees to retrieve specific data based on customer ID or date ranges.

---

## Architecture Overview

The system consists of several core components to handle order and asset management. Key layers include:

- **Authentication & Authorization**: All API requests are validated with admin credentials.
- **Customer Management**: Handles creation, and removal of customers.
- **Order Management**: Handles creation, listing, and cancellation of stock orders.
- **Asset Management**: Manages customer assets and updates balances in real-time during transactions.
- **Transaction Processing**: Ensures secure deposit and withdrawal operations, integrating IBAN verification for secure transfers.

---

## Testing
The implemented code ensures high coverage, which is continuously monitored using `JaCoCo` to ensure the coverage threshold(%82) is met. JaCoCo provides real-time feedback if the coverage limit is violated.

This project is designed to be tested using Postman, a widely-used API testing tool. The API specification file contains all the necessary details to interact with the API and validate its functionality.

---

## Formatting
The project follows the Google Java Format, and the code is automatically checked for adherence to these formatting rules using the `spotless` plugin.

---

## Usage

This API spec file contains all the necessary details regarding the available endpoints, request/response formats, and authentication requirements.

**To interact with the API: [brokagefirm-api-spec.yaml](api-spec/brokagefirm-api-spec.yaml)**

Import the brokagefirm-api-spec.yaml file into tools like Postman or Swagger Editor. Use the defined endpoints to make requests and explore the API functionality.

After running the service in your local environment, you can test the API using Postman or Swagger Editor.