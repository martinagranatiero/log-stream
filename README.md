# 📘 LogStream

**LogStream** è un sistema distribuito per la raccolta e gestione centralizzata dei log provenienti da microservizi o applicazioni.  
Sfrutta Redis Streams per la messaggistica asincrona e PostgreSQL per l'archiviazione persistente, garantendo scalabilità e separazione delle responsabilità tra i servizi.

---

## 🧩 Architettura

[Client App] ──> [Log Gateway Service] ──> [Redis Stream ("log_stream")]

↓

[Log Processor Service]

↓

[PostgreSQL Database]

↓

[Log API Service]


---

## ✨ Componenti

| Servizio        | Descrizione                                                    |
|-----------------|----------------------------------------------------------------|
| **log-gateway** | Espone un endpoint `/logs` (POST) per ricevere log in JSON     |
| **log-processor** | Consuma i log da Redis Stream e li salva in PostgreSQL        |
| **log-api**     | API REST per consultare e filtrare i log archiviati            |

---

## 🧪 Esempio payload (`POST /logs`)

```json
{
  "serviceName": "auth-service",
  "level": "ERROR",
  "message": "User login failed",
  "metadata": {
    "userId": "123",
    "ip": "192.168.1.1"
  }
}
