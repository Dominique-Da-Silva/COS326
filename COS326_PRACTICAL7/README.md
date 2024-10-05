# To Install op Ubuntu WSL
```
cat /etc/lsb-release
```
```
sudo apt-get install gnupg curl
```
```
curl -fsSL https://www.mongodb.org/static/pgp/server-8.0.asc |    sudo gpg -o /usr/share/keyrings/mongodb-server-8.0.gpg    --dearmor
```
```
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-8.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/8.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-8.0.list
```
```
sudo apt-get update
```
```
sudo apt-get install -y mongodb-org
```
```
sudo systemctl start mongod
```
```
sudo systemctl status mongod
```

# Install MongoDB Compass
You can find the link online.
Download it on Windows.

# How to use
Copy paste your javascript file into your ubuntu folder.
```
mongosh
```
```
use "database name"
```
And then you call your individual functions:
```
insertPatients("HospitalDB", "Patients", 10)
```

# Start up
Before opening Compass GUI
```
sudo systemctl restart mongod
```
```
sudo systemctl status mongod
```
