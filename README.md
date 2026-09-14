# Ubuntu App — Quinto Impacto

A sustainable microbusiness discovery and investment platform that connects impact
investors with micro-entrepreneurs generating positive social and environmental impact.

## About

Ubuntu is a platform built by **Quinto Impacto**, a student team from **Semillero**
(a youth tech training program) in Mendoza, Argentina. Its mission is to develop and
manage instruments that connect impact investors with micro-entrepreneur projects by
showcasing their purpose, context, and history — motivating investors to financially
support companies and organizations that generate positive impact.

Impact investors struggle to find micro-entrepreneurs whose mission aligns with their
values, while micro-entrepreneurs lack visibility beyond their local community. Ubuntu
solves this with a curated catalog of microbusinesses that investors can browse by
category, search by name, or discover nearby using geolocation. When an investor finds
a project they like, they submit a contact request so the relationship can begin.

The platform serves three audiences:

- **Visitors** — browse microbusinesses and publications, ask the chatbot, and submit
  contact requests without an account.
- **Users** — log in with Google (passwordless) to manage their profile and newsletter
  subscription.
- **Admins** — curate the full catalog: microbusinesses, publications, categories,
  contact requests, users, and images.

## Features

**Microbusiness directory** — searchable catalog of micro-entrepreneurs with location
data, categories, subcategories, and images. Includes a "nearby" search based on the
visitor's coordinates and an admin-controlled visibility toggle.

**Publications** — admin-managed news and articles about the platform, with
full-text search, per-article view tracking for analytics, and an active/inactive
visibility toggle.

**Chatbot FAQ** — helps visitors navigate the platform and learn about Ubuntu and
investing. Answers both browsed questions (by category) and free-form natural
language questions using tokenization plus cosine similarity — no LLM involved.

**Investor contact requests** — visitors submit their details and a message for a
specific microbusiness. Admins review pending requests, mark them as reviewed, and
consult monthly statistics.

**Google OAuth2 login** — passwordless authentication via Google. Issues a short-lived
JWT access token and a rotating refresh token stored as secure HTTP-only cookies, with
automatic first-login registration.

**Image management** — upload, replace, and delete images hosted on Cloudinary for
microbusinesses, publications, and user profile photos.

**Weekly newsletter** — every Friday morning, admins receive an HTML digest of newly
added microbusinesses.

**Rate limiting** — Bucket4j filter protects all API endpoints against abuse.

**Swagger API docs** — 42 endpoints documented across 9 domain-grouped OpenAPI specs.

## Domain Model

**User** — `id`, `firstName`, `lastName`, `email`, `phone`, `image` (Cloudinary URL),
`active`, `role` (`ADMIN`, `USER`, `VISITOR`), `subscribed` (newsletter opt-in). No
password field: authentication is Google OAuth only. Implements Spring Security
`UserDetails`.

**Microbusiness** — `id`, `name`, `description` (300 chars), `moreInfo` (300 chars),
`country`, `province`, `city`, `subcategory`, `active` (visibility toggle), `mailed`
(newsletter inclusion flag), `createdDate`. Many-to-one with `Category`, one-to-many
with `Image`. Location is stored as plain strings, not foreign keys.

**Category** — `id`, `name`. Four seeded categories: social economy / local
development / financial inclusion; agroecology / organic / healthy food;
conservation / regeneration / ecosystem services; impact companies / circular economy.

**Publication** — `id`, `title`, `description` (2000 chars), `date`, `active`.
One-to-many with `Image` and with `PublicationView`.

**PublicationView** — `id`, `clickDate`, many-to-one with `Publication`. Powers the
top-publications analytics.

**ContactRequest** — `id`, `fullName`, `email`, `phone`, `message` (300 chars),
`reviewed`, `requestDate`, many-to-one with `Microbusiness`. The investor inquiry
mechanism.

**Image** — `id`, `url`. Shared by microbusinesses and publications.

**Country / Province** — reference data for geographic dropdowns. A country has many
provinces.

**ChatbotResponse / ChatbotQuestion** — a response (`id`, `answer` up to 1000 chars)
has many questions (`id`, `question` up to 400 chars, `category` enum:
`INSTITUTIONAL`, `MICROBUSINESS`, `FREQUENT_QUESTIONS`).

**RefreshToken** — `id`, `tokenHash`, `userEmail`, `expiresAt`, `revoked`,
`createdAt`. Supports refresh token rotation and revocation on logout.

Entity relationships:

```text
User (standalone, no FK to other domain entities)
Category  <1---*  Microbusiness  *---<  Image
                  Microbusiness  *---<  ContactRequest
Publication  *---<  PublicationView
Publication  *---<  Image
ChatbotResponse  *---<  ChatbotQuestion
Country  *---<  Province
```

## Tech Stack

| Component  | Technology                             |
|------------|----------------------------------------|
| Runtime    | Java 21                                |
| Framework  | Spring Boot 4.1.0                      |
| Database   | PostgreSQL 18 + Flyway                 |
| Auth       | Google OAuth2 (OIDC) + JWT             |
| Images     | Cloudinary                             |
| Geocoding  | Nominatim (OpenStreetMap) + Caffeine   |
| NLP        | Apache Lucene (cosine similarity)      |
| Email      | JavaMailSender + Thymeleaf             |
| Rate limit | Bucket4j                               |
| API docs   | SpringDoc OpenAPI                      |
| Build      | Maven 3.9                              |
| Container  | Docker multi-stage (Alpine + jlink)    |
| License    | Apache 2.0                             |

## Getting Started

### Prerequisites

- Java 21 (JDK)
- Maven 3.9+ (or use the included `mvnw` wrapper)
- PostgreSQL 18 (or Docker)
- A Google Cloud Console project with OAuth2 credentials
- A Cloudinary account (for image hosting)

### With Docker Compose (recommended)

1. Copy the environment template and fill in all required values:

   ```bash
   cp variables.env-template variables.env
   ```

2. Start all services:

   ```bash
   docker compose up -d
   ```

3. Verify the app is healthy:

   ```bash
   curl http://localhost:8080/actuator/health
   ```

4. Open the API docs: http://localhost:8080/swagger-ui.html

### Local development

1. Copy the environment template and fill in all required values:

   ```bash
   cp variables.env-template variables.env
   ```

2. Create the PostgreSQL database:

   ```bash
   createdb ubuntu
   ```

3. Run the application (Flyway migrations run automatically on startup):

   ```bash
   ./mvnw spring-boot:run
   ```

## API Overview

Base URL: `http://localhost:8080`. Full reference at `/swagger-ui.html`.
Every response uses the `ApiResponse` envelope (`status`, `message`, `data`,
`timestamp`).

### Auth

| Method | Path                 | Access | Description             |
|--------|----------------------|--------|-------------------------|
| POST   | /api/v1/auth/login   | Public | Validate credentials    |
| POST   | /api/v1/auth/refresh | Public | Rotate refresh token    |
| POST   | /api/v1/auth/logout  | Public | Revoke and clear tokens |

### Microbusinesses

| Method | Path                                      | Access | Description          |
|--------|-------------------------------------------|--------|----------------------|
| GET    | /api/v1/microbusiness                     | Public | List by category     |
| GET    | /api/v1/microbusiness/search              | Public | Search by name       |
| GET    | /api/v1/microbusiness/near                | Public | Find nearby (lat/lon)|
| POST   | /api/v1/microbusiness                     | Admin  | Create               |
| PUT    | /api/v1/microbusiness/{id}                | Admin  | Update               |
| DELETE | /api/v1/microbusiness/{id}                | Admin  | Delete               |
| PUT    | /api/v1/microbusiness/{id}/visibility     | Admin  | Toggle visibility    |
| GET    | /api/v1/microbusiness/statistics/monthly  | Admin  | Monthly count        |
| GET    | /api/v1/microbusiness/statistics/by-category | Admin | Per-category count |

### Publications

| Method | Path                                 | Access | Description          |
|--------|--------------------------------------|--------|----------------------|
| GET    | /api/v1/publications?active=true     | Public | List publications    |
| GET    | /api/v1/publications/{id}            | Public | Get by ID            |
| GET    | /api/v1/publications/search?q=...    | Public | Full-text search     |
| POST   | /api/v1/publications/{id}/views      | Public | Record a view        |
| POST   | /api/v1/publications                 | Admin  | Create               |
| PUT    | /api/v1/publications/{id}            | Admin  | Update               |
| DELETE | /api/v1/publications/{id}            | Admin  | Delete               |
| PUT    | /api/v1/publications/{id}/visibility | Admin  | Toggle visibility    |
| GET    | /api/v1/publications/statistics/top  | Admin  | Top by view count    |

### Chatbot

| Method | Path                         | Access | Description              |
|--------|------------------------------|--------|--------------------------|
| GET    | /api/v1/chatbot/questions    | Public | List questions by category |
| GET    | /api/v1/chatbot/answers/{id} | Public | Answer for a question    |
| GET    | /api/v1/chatbot/answers      | Public | Ask a free-form question |

### Contact requests

| Method | Path                                  | Access | Description          |
|--------|---------------------------------------|--------|----------------------|
| POST   | /api/v1/contact-requests?microId=1    | Public | Submit a request     |
| GET    | /api/v1/contact-requests?reviewed=false | Admin | List by review status |
| GET    | /api/v1/contact-requests/{id}         | Admin  | Get details          |
| PUT    | /api/v1/contact-requests/{id}/review  | Admin  | Mark as reviewed     |
| GET    | /api/v1/contact-requests/statistics   | Admin  | Monthly counts       |

### Users

| Method | Path                        | Access       | Description        |
|--------|-----------------------------|--------------|--------------------|
| POST   | /api/v1/users               | Admin        | Register user      |
| GET    | /api/v1/users               | Admin        | List all users     |
| PUT    | /api/v1/users/{id}          | Admin / User | Update profile     |
| PUT    | /api/v1/users/{id}/deactivate | Admin / User | Deactivate account |

### Categories, countries, provinces

| Method | Path               | Access | Description               |
|--------|--------------------|--------|---------------------------|
| GET    | /api/v1/categories | Public | List all categories       |
| POST   | /api/v1/categories | Admin  | Create category           |
| GET    | /api/v1/countries  | Public | List all countries        |
| GET    | /api/v1/provinces  | Public | List provinces by country |

### Media (Cloudinary)

| Method | Path                      | Access | Description        |
|--------|---------------------------|--------|--------------------|
| POST   | /api/v1/cloudinary/images | Admin  | Upload images      |
| PUT    | /api/v1/cloudinary/images | Admin  | Replace image      |
| DELETE | /api/v1/cloudinary/images | Admin  | Delete image       |

## Architecture

The codebase follows hexagonal architecture (ports and adapters), organized in
three top-level packages: `application/` holds the core business logic as use
cases plus inbound/outbound port interfaces with no framework dependencies;
`infrastructure/` implements those ports with JPA adapters, MapStruct mappers,
entities, repositories, seeders, and the REST web layer; `shared/` contains
cross-cutting concerns such as security, configuration properties, the API
response envelope, error handling, geolocation, rate limiting, and OpenAPI docs.

## Testing

```bash
mvn clean test
```

## License

Apache License 2.0 — see [LICENSE](LICENSE).
