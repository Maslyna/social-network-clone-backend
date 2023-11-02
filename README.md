# SOCIAL-NETWORK-CLONE

###### This is simple code of default social network. I prefer to name it "parody" on different social network like: Twitter (or just X), Facebook, Linkedin, etc.

---

## Technologies stack:

- Spring Boot
- Spring Cloud
- Spring Data JPA
- PostgresSQL
- Kafka
- GCP Storage

---

## Main goals:

- [X] User Authentication and Authorization:

> Implement a secure authentication system to allow users to create accounts, log in, and log out.
> Use token-based authentication to ensure secure access to user-specific data. Also, manage authorization to control
> what actions users can perform.

- [X] User Profile Management

> Create endpoints to handle user profiles. Users should be able to update their profiles,
> change passwords, and manage account settings.

- [X] Post Creation and Interaction

> Implement functionality for users to create, edit, and delete posts. Allow users to like, comment on, and share posts.
> Consider features like attaching images or other media to posts.

- [X] Following and Followers

> Design a system for users to follow and unfollow each other. This is crucial for building a social network where
> users can see content from people they follow.

- [X] Notifications

> Implement a notification system to inform users about interactions on their posts, new followers,
> and other relevant activities.

- [X] Hashtags and Search

> Allow users to add hashtags to their posts for categorization. Implement a search functionality
> so users can discover posts based on hashtags or other criteria.

---

## Project structure

![project-schema.png](images/project-schema.png)

---

## TESTs

#### Global test coverage:

TODO: make global test coverage

#### 1. SECURITY-SERVICE coverage:

![security-service-test-coverage.png](images/security-service-test-coverage.png)

#### 2. POST-SERVICE coverage:

![post-service-coverage.png](images/post-service-test-coverage.png)

#### 3. FOLLOWER-SERVICE coverage

![](images/follower-service-test-coverage.png)