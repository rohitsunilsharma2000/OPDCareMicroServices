To check table data in a MySQL database using the command line, you can follow these steps:

---

### ✅ **1. Access the MySQL container (if using Docker):**
If you're using Docker, first enter the running MySQL container:

```bash
docker exec -it mysql-container mysql -u root -p
```

When prompted, enter the password (in your case, `SYSTEM`).

---

### ✅ **2. Select the database:**
```sql
USE turingonlineforumsystem;
```

---

### ✅ **3. List the tables:**
```sql
SHOW TABLES;
```

---

### ✅ **4. View table data:**
```sql
SELECT * FROM your_table_name;
```

Replace `your_table_name` with the actual table name you want to inspect.

---

### ✅ Bonus: Exit MySQL shell
```sql
exit;
```

