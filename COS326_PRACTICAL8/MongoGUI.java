package mongopractical7;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MongoGUI extends JFrame {
    private JButton getProductListButton;
    private JTextArea productListArea;
    private JButton countProductsButton;
    private JButton findExpensiveProductsButton;
    private JButton extractReviewsButton;
    private JButton viewReviewsButton;
    private JButton getCollectionNamesButton;

    public MongoGUI(){
        super();

        getProductListButton = new JButton("Get Product List");
        countProductsButton = new JButton("Count Products");
        findExpensiveProductsButton = new JButton("Find Expensive Products");
        extractReviewsButton = new JButton("Extract Reviews");
        viewReviewsButton = new JButton("View Reviews Collection");
        getCollectionNamesButton = new JButton("Get Collection Names");
        
        getProductListButton.addActionListener(e -> fetchProducts());
        countProductsButton.addActionListener(e -> countProducts());
        findExpensiveProductsButton.addActionListener(e -> findExpensiveProducts());
        extractReviewsButton.addActionListener(e -> extractReviews());
        viewReviewsButton.addActionListener(e -> viewReviews());
        getCollectionNamesButton.addActionListener(e -> getCollectionNames());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(getProductListButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        buttonPanel.add(countProductsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(findExpensiveProductsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(extractReviewsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(viewReviewsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(getCollectionNamesButton);

        // Output area for displaying results
        productListArea = new JTextArea(20, 40);
        productListArea.setEditable(false);  // Make it read-only
        JScrollPane scrollPane = new JScrollPane(productListArea);  // Make the text area scrollable

        // Set layout for the JFrame
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.WEST);  // Buttons on the left
        add(scrollPane, BorderLayout.CENTER);  // Text area on the right

        // Window settings
        setTitle("MongoDB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    /*
    --------------------------------------------------------------------
    Function 1: fetching all products in the collection
    --------------------------------------------------------------------
    */
    private void fetchProducts() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();
        
        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            MongoCollection<Document> collection = db.getCollection("products");

            StringBuilder formattedProductList = new StringBuilder();
            collection.find().forEach(document -> {
                List<Document> products = document.getList("products", Document.class);

                for (Document product : products) {
                    formattedProductList.append("Product ID: ").append(product.getString("product_id")).append("\n");
                    formattedProductList.append("Name: ").append(product.getString("name")).append("\n");
                    formattedProductList.append("Category: ").append(product.getString("category")).append("\n");

                    Object price = product.get("price");
                    if (price instanceof Integer) {
                        formattedProductList.append("Price: R").append((Integer) price).append("\n");
                    } else if (price instanceof Double) {
                        formattedProductList.append("Price: R").append((Double) price).append("\n");
                    }

                    formattedProductList.append("Stock: ").append(product.getInteger("stock")).append(" units\n");

                    List<Document> reviews = product.getList("reviews", Document.class);
                    if (reviews != null && !reviews.isEmpty()) {
                        formattedProductList.append("Reviews:\n");
                        for (Document review : reviews) {
                            formattedProductList.append("  - ").append(review.getString("review"))
                                    .append(" (Rating: ").append(review.getInteger("rating")).append("/5)\n");
                        }
                    }
                    formattedProductList.append(" \n");
                    formattedProductList.append("===================================\n");
                    formattedProductList.append(" \n");
                }
            });

            if (formattedProductList.length() == 0) {
                productListArea.setText("No products found.");
            } else {
                productListArea.setText(formattedProductList.toString());
            }
        } catch (Exception e) {
            productListArea.setText("Error: Unable to retrieve products.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    Function 2: counting the number of products in the products collection
    --------------------------------------------------------------------
    */
    private void countProducts() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();
        
        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            MongoCollection<Document> collection = db.getCollection("products");

            long totalProductCount = 0;
            for (Document document : collection.find()) {
                List<Document> products = document.getList("products", Document.class);
                if (products != null) {
                    totalProductCount += products.size();
                }
            }

            productListArea.setText("Total number of products: " + totalProductCount);
        } catch (Exception e) {
            productListArea.setText("Error: Unable to count products.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    Function 3: finding all the products above the 500 price range
    --------------------------------------------------------------------
    */
    private void findExpensiveProducts() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();

        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            MongoCollection<Document> collection = db.getCollection("products");

            StringBuilder expensiveProductsList = new StringBuilder();
            collection.find().forEach(document -> {
                List<Document> products = document.getList("products", Document.class);

                for (Document product : products) {
                    Object price = product.get("price");
                    double productPrice = (price instanceof Integer) ? ((Integer) price).doubleValue() : (Double) price;

                    if (productPrice > 500) {
                        expensiveProductsList.append("Product ID: ").append(product.getString("product_id")).append("\n");
                        expensiveProductsList.append("Name: ").append(product.getString("name")).append("\n");
                        expensiveProductsList.append("Category: ").append(product.getString("category")).append("\n");
                        expensiveProductsList.append("Price: R").append(productPrice).append("\n");
                        expensiveProductsList.append(" \n");
                        expensiveProductsList.append("===================================\n");
                        expensiveProductsList.append(" \n");
                    }
                }
            });

            if (expensiveProductsList.length() == 0) {
                productListArea.setText("No products with price greater than R500 found.");
            } else {
                productListArea.setText(expensiveProductsList.toString());
            }
        } catch (Exception e) {
            productListArea.setText("Error: Unable to retrieve expensive products.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    Function 4: extracting the reviews and creating new collection
    --------------------------------------------------------------------
    */
    private void extractReviews() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();
        
        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            MongoCollection<Document> productsCollection = db.getCollection("products");
            MongoCollection<Document> reviewsCollection = db.getCollection("reviews");

            productsCollection.find().forEach(productDocument -> {
                List<Document> products = productDocument.getList("products", Document.class);

                for (Document product : products) {
                    String productId = product.getString("product_id");
                    String productName = product.getString("name");
                    List<Document> reviews = product.getList("reviews", Document.class);

                    if (reviews != null) {
                        for (Document review : reviews) {
                            Document reviewDoc = new Document("product_id", productId)
                                    .append("product_name", productName)
                                    .append("review", review.getString("review"))
                                    .append("rating", review.getInteger("rating"));

                            reviewsCollection.insertOne(reviewDoc);
                        }
                    }
                }
            });

            productListArea.setText("Reviews have been extracted to the 'reviews' collection.");
        } catch (Exception e) {
            productListArea.setText("Error: Unable to extract reviews.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    Function 5: showing the new created reviews collection
    --------------------------------------------------------------------
    */
    private void viewReviews() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();
        
        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            MongoCollection<Document> reviewsCollection = db.getCollection("reviews");

            StringBuilder reviewsList = new StringBuilder();
            reviewsCollection.find().forEach(reviewDocument -> {
                reviewsList.append("Product ID: ").append(reviewDocument.getString("product_id")).append("\n");
                reviewsList.append("Product Name: ").append(reviewDocument.getString("product_name")).append("\n");
                reviewsList.append("Review: ").append(reviewDocument.getString("review")).append("\n");
                reviewsList.append("Rating: ").append(reviewDocument.getInteger("rating")).append("/5\n");
                reviewsList.append(" \n");
                reviewsList.append("===================================\n");
                reviewsList.append(" \n");
            });

            if (reviewsList.length() == 0) {
                productListArea.setText("No reviews found in the 'reviews' collection.");
            } else {
                productListArea.setText(reviewsList.toString());
            }
        } catch (Exception e) {
            productListArea.setText("Error: Unable to retrieve reviews.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    Function 6: names of all the collections in the database
    --------------------------------------------------------------------
    */

    private void getCollectionNames() {
        productListArea.setText("");  // Clear the previous output
        MongoDatabase db = MongoDBConnection.connect();
        
        // Check if the database connection is null (indicating a connection failure)
        if (db == null) {
            productListArea.setText("Error: Unable to connect to the database.");
            return;
        }

        try {
            StringBuilder collectionNamesList = new StringBuilder("Collections in the database:\n");
            db.listCollectionNames().forEach(collectionName -> collectionNamesList.append(collectionName).append("\n"));
            
            if (collectionNamesList.length() == 0) {
                productListArea.setText("No collections found in the database.");
            } else {
                productListArea.setText(collectionNamesList.toString());
            }
        } catch (Exception e) {
            productListArea.setText("Error: Unable to retrieve collection names.");
        }
    }
    
    /*
    --------------------------------------------------------------------
    */
}
