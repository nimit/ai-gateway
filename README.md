# AI Gateway

AI Gateway is a secure, highly scalable, containerized & open-source API service.

> ONE KEY TO RULE THEM ALL

Just use the gateway's API key and you can access models from different providers (e.g. OpenAI, Anthropic, etc) without caring about multiple API keys.
Automatically switch to the best model (or one with free quota remaining) for the task, port chat history across different models and best of all, view highly detailed graphs charting your usage.

## Tech
- REQUIREMENT: Secure storage of API keys (most important)
- Spring Boot under the hood
- PostgreSQL (with db migrations)
- Redis as a quota caching layer
- Apache Kafka - logging for analytics
- Grafana to visualize a dashboard

## Roadmap

1. ~~Setup containerized development environment & distribution~~
2. ~~Basic Spring Boot project~~
3. ~~Database setup~~
4. ~~Custom magic link authentication & account creation~~
5. Framework to add different providers
6. Analytics with Kafka
7. Grafana dashboard

\[ONGOING PROJECT]

AI gateway is built from the groud up to be as easy to setup and develop as possible, use VSCode's devcontainers to setup a development environement with a single command.
