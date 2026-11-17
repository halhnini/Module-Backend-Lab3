# Product API

API REST Spring Boot 3.2 (Java 21) de gestion de produits, catégories et commandes, avec JPA, Hibernate et MySQL.

## Prérequis

- Docker Desktop

Pour lancer le projet sans Docker : JDK 21, Maven et un MySQL accessible sur `localhost:3307`.

## Lancer avec Docker

```bash
docker compose up -d --build
```

- API : http://localhost:8080
- MySQL : `localhost:3307`, base `productdb`, utilisateur `root` / `root`

Arrêter :

```bash
docker compose down
```

Arrêter et effacer la base :

```bash
docker compose down -v
```

## Données de test

`src/main/resources/db/data.sql` est chargé à chaque démarrage : 2 catégories (Électronique, Livres) et 3 produits. Le script n'insère rien en double.

`src/main/resources/db/create-database.sql` crée la base à la main (inutile avec Docker).

## Endpoints

### Produits

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/products?page=0&size=10&sort=id` | Liste paginée et triée |
| GET | `/api/products/{id}` | Un produit |
| GET | `/api/products/search?name=...` | Recherche par nom |
| POST | `/api/products` | Création (201) |
| PUT | `/api/products/{id}` | Modification |
| DELETE | `/api/products/{id}` | Suppression (204) |

### Catégories

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/categories` | Liste |
| GET | `/api/categories/{id}` | Une catégorie |
| GET | `/api/categories/{id}/products` | Produits de la catégorie |
| POST | `/api/categories` | Création (201) |

### Commandes

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/orders/{id}` | Une commande avec ses articles |
| POST | `/api/orders` | Création (201) |

## Exemples

```bash
curl -X POST http://localhost:8080/api/categories -H "Content-Type: application/json" -d "{\"name\":\"Audio\"}"
```

```bash
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d "{\"name\":\"Smartphone\",\"price\":699.99,\"categoryId\":1}"
```

```bash
curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d "{\"items\":[{\"productId\":1,\"quantity\":2}]}"
```

La collection `Lab3-API.postman_collection.json` contient les mêmes appels pour Postman.

## Règles de validation

- Produit : `name` obligatoire et unique, `price` > 0, `quantity` entre 0 et 1000 (0 par défaut), `categoryId` facultatif
- Catégorie : `name` obligatoire
- Commande : au moins un article, chaque article avec un `productId` existant et une quantité > 0

