# PROJECT SETUP
Open you ubuntu terminal.
```
sudo systemctl restart mongod
```

1. Open MongoDB Compass:
If you don’t have it installed, download it from here.

2. Connect to your MongoDB Database:
In MongoDB Compass, connect to the same MongoDB instance where you created the ecommercedb database.

3. Select the Database:
In the left-side panel, click on ecommercedb to view its collections.

4. Open the Products Collection:
Inside ecommercedb, you should see the products collection (you may need to refresh the view if you just created it).
Click on the products collection.

6. Import the JSON File:
In the toolbar, click on the … (More Options) button or Collection dropdown and choose Import Data.
In the popup window, choose Select File, and navigate to the EcommerceData.json file.
Select JSON as the file type.
Click Import to load the JSON data into the products collection.

8. Verify the Data:
After importing, check the products collection in Compass to ensure that the data was correctly inserted.
