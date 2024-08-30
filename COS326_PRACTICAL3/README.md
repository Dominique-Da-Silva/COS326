# Installation of Postgres server on WSL and using pgAdmin.
---
## Open your Ubuntu terminal
* sudo apt install -y postgresql-common 
* sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh
* sudo apt update
* sudo apt -y install postgresql
* sudo systemctl status postgres
* sudo -u postgres createuser <username>
* sudo -u postgres createdb <dbname>

## Now enter the postgres command line
* sudo -u postgres psql
* ALTER USER <username> WITH PASSWORD 'username';
* ALTER ROLE <username> WITH SUPERUSER;
* GRANT ALL PRIVILEGES ON DATABASE <dbname> TO <username>
* GRANT CREATE ON SCHEMA public TO <username>;
* SELECT rolname, rolsuper, rolinherit, rolcreaterole, rolcreatedb, rolcanlogin FROM pg_roles WHERE rolname = 'your_username';

---

Then type \q to exit the postgres server.

Open up pgadmin and get to work.

Remember to delete the following before the demo:
* tables
* functions
* types
* domains
* sequences
