
# Sponsored Ads Module - Spring Project

## Overview

This project is an implementation of a Sponsored Ads module using Spring,
enabling sellers to create campaigns for promoting their products. It includes APIs to manage campaigns, products, and serving ads based on categories.

## Features

- **Create Campaign API:** Allows users to create campaigns with specified parameters including name, start date, product identifiers, and bid.
- **Serve Ad API:** Retrieves ads based on the specified category, serving the promoted product with the highest bid from active campaigns in that category.

## Getting Started

1. **Prerequisites:**
    - JDK 11 or higher
    - Gradle
    - Redis server (running locally or on a reachable server)

2. **Configuration:**


## Endpoints

- **POST /products:** Create a new product.
- **GET /products/{productName}:** Get details of a specific product by name.
- **GET /allProducts:** Get all Products

- **POST /campaign:** Create a new campaign.
- **GET /campaigns/{name}:** Get details of a specific campaign by name.
- **GET /allCampaigns:** Get all Campaigns
- **GET /serveAd/{category}:** get an category for ads based on the specified category.

## Usage

- Use any HTTP client like Postman to send requests to the specified endpoints.
- Create products using the `/products` endpoint.
- Create campaigns using the `/campaigns` endpoint.
- Retrieve served ads using the `/serveAd` endpoint based on categories.

## Notes

- Ensure Redis is properly configured and running before starting the application.
- Follow API specifications for request formats when creating products and campaigns.
- Verify that the provided endpoints respond correctly and with expected data.

## Authors

- [eliParser]

---

