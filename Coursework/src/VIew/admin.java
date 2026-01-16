/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package VIew;

/**
 *
 * @author Suyoug Subedi
 */
import Controller.AdminController;
import Model.OrderModel;
import Model.UserStore;
import Model.UserModel;
import javax.swing.JOptionPane;
import java.util.LinkedList;


public class admin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(admin.class.getName());

    /**
     * Creates new form admin
     */
    // DATA STRUCTURES
    private java.util.Queue<UserModel> recentFoodsQueue = new java.util.LinkedList<>();

private java.util.Queue<OrderModel> pendingOrders = new java.util.LinkedList<>();
private java.util.Stack<OrderModel> orderHistory = new java.util.Stack<>();

    private AdminController controller;
private LinkedList<UserModel> menuList;

    private UserStore store;

public admin(UserStore store) {
    initComponents();
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable4.getModel();

model.setRowCount(0); // clear existing rows

    this.store = store;

// create controller only once
controller = new AdminController(store);

// seed default menu items only once
store.seedIfEmpty();

// get menu list from store
menuList = store.getUsers();

// initialise UI components
initCategoryCombo();
loadTable();

    loadTableFromMenuList();
    this.store = store;

this.store.seedIfEmpty();          // load default foods once
this.menuList = this.store.getUsers(); // get menu list

setupRecentFoodsTable();           // set columns
refreshRecentFoods();              // show last 5 in tblRecentFoods

}
private void setupRecentFoodsTable() {
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        new Object[][]{},
        new String[]{"ID", "Food Name", "Category", "Price"}
    ) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false; // read-only
        }
    };

    jTable4.setModel(model);
}

public void refreshRecentFoods() {
    recentFoodsQueue.clear();

    if (menuList == null || menuList.isEmpty()) {
        ((javax.swing.table.DefaultTableModel) jTable4.getModel()).setRowCount(0);
        return;
    }

    int start = Math.max(0, menuList.size() - 5);

    for (int i = start; i < menuList.size(); i++) {
        recentFoodsQueue.add(menuList.get(i));
    }

    loadRecentFoodsTable();
}

private void loadRecentFoodsTable() {
    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) jTable4.getModel();

    model.setRowCount(0);

    for (UserModel m : recentFoodsQueue) {
        model.addRow(new Object[]{
            m.getId(),
            m.getName(),
            m.getCategory(),
            m.getPrice()
        });
    }
}

public void selectionSortById() {
    int n = menuList.size();
    for (int i = 0; i < n - 1; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++) {
            if (menuList.get(j).getId()
                    .compareToIgnoreCase(menuList.get(min).getId()) < 0) {
                min = j;
            }
        }
        UserModel temp = menuList.get(min);
        menuList.set(min, menuList.get(i));
        menuList.set(i, temp);
    }
}
private void loadOrderHistoryTable() {
    javax.swing.table.DefaultTableModel tm =
        (javax.swing.table.DefaultTableModel) jTable3.getModel(); // history table name

    tm.setRowCount(0);

    for (int i = controller.getOrderHistory().size() - 1; i >= 0; i--) {
        OrderModel o = controller.getOrderHistory().get(i);
        tm.addRow(new Object[]{
            o.getItemName(),
            o.getQuantity(),
            o.getPrice()
        });
    }
}

public java.util.LinkedList<UserModel> linearSearchMenu(String query) {
    java.util.LinkedList<UserModel> result = new java.util.LinkedList<>();
    if (query == null) return result;

    String q = query.trim().toLowerCase();
    if (q.isEmpty()) return result;

    for (UserModel m : menuList) {
        String id = (m.getId() == null) ? "" : m.getId().toLowerCase();
        String name = (m.getName() == null) ? "" : m.getName().toLowerCase();
        String cat = (m.getCategory() == null) ? "" : m.getCategory().toLowerCase();

        if (id.contains(q) || name.contains(q) || cat.contains(q)) {
            result.add(m);
        }
    }
    return result;
}

private UserModel binarySearchById(String id) {

    if (id == null || id.trim().isEmpty()) {
        return null;
    }

    
    selectionSortById();

    String target = id.trim().toLowerCase();
    int low = 0;
    int high = menuList.size() - 1;

    while (low <= high) {
        int mid = (low + high) / 2;

        String midId = menuList.get(mid).getId().toLowerCase();
        int cmp = midId.compareTo(target);

        if (cmp == 0) {
            return menuList.get(mid);   // found
        } else if (cmp < 0) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return null; // not found
}


public void selectionSortByName() {
    int n = menuList.size();

    for (int i = 0; i < n - 1; i++) {
        int minIndex = i;

        for (int j = i + 1; j < n; j++) {
            if (menuList.get(j).getName()
                    .compareToIgnoreCase(menuList.get(minIndex).getName()) < 0) {
                minIndex = j;
            }
        }

        UserModel temp = menuList.get(minIndex);
        menuList.set(minIndex, menuList.get(i));
        menuList.set(i, temp);
    }
    
}



private void loadTableFromMenuList() {
    javax.swing.table.DefaultTableModel tm =
        (javax.swing.table.DefaultTableModel) jTable1.getModel(); 

    tm.setRowCount(0);

    for (UserModel u : menuList) {
        tm.addRow(new Object[]{
            u.getId(),
            u.getName(),
            u.getPrice(),
            u.getCategory()
        });
    }
}
private void loadTableFromList(java.util.LinkedList<UserModel> list) {
    javax.swing.table.DefaultTableModel tm =
        (javax.swing.table.DefaultTableModel) jTable1.getModel();
    tm.setRowCount(0);

    for (UserModel u : list) {
        tm.addRow(new Object[]{u.getId(), u.getName(), u.getPrice(), u.getCategory()});
    }
}



public void insertionSortByPrice() {
    int n = menuList.size();

    for (int i = 1; i < n; i++) {
        UserModel key = menuList.get(i);
        int j = i - 1;

        while (j >= 0 && menuList.get(j).getPrice() > key.getPrice()) {
            menuList.set(j + 1, menuList.get(j));
            j--;
        }
        menuList.set(j + 1, key);
    }
}




 private String validateFood(boolean requireAllFields) {
    // Validates food form inputs

    String id = jTextField1.getText().trim();      // Food ID
    String food = jTextField2.getText().trim();   // Food Name
    String priceTxt = jTextField3.getText().trim(); // Price
    Object catObj = jComboBox1.getSelectedItem(); // Category

    if (id.isEmpty()) return "Food ID is required.";

    if (requireAllFields) {
        if (food.isEmpty()) return "Food name is required.";
        if (catObj == null) return "Category is required.";
        if (priceTxt.isEmpty()) return "Price is required.";
    }

    if (!food.isEmpty() && food.length() < 2)
        return "Food name must be at least 2 characters.";

    if (!priceTxt.isEmpty()) {
        try {
            double p = Double.parseDouble(priceTxt);
            if (p <= 0) return "Price must be greater than 0.";
        } catch (NumberFormatException e) {
            return "Price must be a valid number (e.g., 120 or 120.5).";
        }
    }

    return "OK";
}

private double getPriceValue() {
    // Converts price text to double
    return Double.parseDouble(jTextField3.getText().trim());
}
private void initCategoryCombo() {
    jComboBox1.removeAllItems();
    jComboBox1.addItem("Main Course");
    jComboBox1.addItem("Beverage");
    jComboBox1.addItem("Fast Food");
    jComboBox1.addItem("Snacks");
    jComboBox1.addItem("Dessert");
}

private void clearFoodFields() {
    // Clears all food input fields
    jTextField1.setText("");
    jTextField2.setText("");
    jTextField3.setText("");
    jComboBox1.setSelectedIndex(0);
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        update = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        orders = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton14 = new javax.swing.JButton();
        user = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 0));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Admin Dashboard");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Canteen Management System");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/resized.png"))); // NOI18N
        jLabel11.setText("jLabel11");

        jButton7.setText("Logout");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(48, 48, 48))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 0, 204));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButton4.setText("View Orders");
        jButton4.addActionListener(this::jButton4ActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 73, 5, 0);
        jPanel2.add(jButton4, gridBagConstraints);

        jButton5.setText("Update");
        jButton5.addActionListener(this::jButton5ActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 53, 5, 0);
        jPanel2.add(jButton5, gridBagConstraints);

        jButton6.setText("Order History");
        jButton6.addActionListener(this::jButton6ActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 69, 5, 83);
        jPanel2.add(jButton6, gridBagConstraints);

        jButton8.setText("Home");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 44, 5, 0);
        jPanel2.add(jButton8, gridBagConstraints);

        jPanel3.setLayout(new java.awt.CardLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setText("Welcome Admin,");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/resized.png"))); // NOI18N
        jLabel13.setText("jLabel11");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("KhajaHub ");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Price ", "Category"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jLabel15.setText("Recently Added Items");

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel15))))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        jPanel3.add(home, "card5");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setText("Item ID");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Item  Name");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Item Price");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Item Category");

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Item Price", "Item Category"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Menu");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Admin operations");

        jButton1.setText("Update");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Add");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Delete");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton9.setText("Sort By Name");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton10.setText("Sort By Price");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jLabel16.setText("Linear Search:");

        jLabel17.setText("Binary Search by ID:");

        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jButton11.setText("Search");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jButton12.setText("Search");
        jButton12.addActionListener(this::jButton12ActionPerformed);

        jButton13.setText("Reset Search");
        jButton13.addActionListener(this::jButton13ActionPerformed);

        javax.swing.GroupLayout updateLayout = new javax.swing.GroupLayout(update);
        update.setLayout(updateLayout);
        updateLayout.setHorizontalGroup(
            updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateLayout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(updateLayout.createSequentialGroup()
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updateLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(updateLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(12, 12, 12)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(updateLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updateLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(updateLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(33, 33, 33)
                                .addComponent(jButton2)
                                .addGap(28, 28, 28)
                                .addComponent(jButton3)))))
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(25, 25, 25)))
                                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(17, 17, 17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addGap(14, 14, 14)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton10)
                                .addGap(69, 69, 69))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateLayout.createSequentialGroup()
                                .addComponent(jButton13)
                                .addGap(160, 160, 160))))))
        );
        updateLayout.setVerticalGroup(
            updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel6)
                .addGap(2, 2, 2)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(updateLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(192, 192, 192)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(49, 106, Short.MAX_VALUE))
            .addGroup(updateLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(updateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(update, "card4");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Orders");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Username", "Item ID", "Item Name", "Item Quantity", "Item Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton14.setText("Complete Order");
        jButton14.addActionListener(this::jButton14ActionPerformed);

        javax.swing.GroupLayout ordersLayout = new javax.swing.GroupLayout(orders);
        orders.setLayout(ordersLayout);
        ordersLayout.setHorizontalGroup(
            ordersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersLayout.createSequentialGroup()
                .addGroup(ordersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ordersLayout.createSequentialGroup()
                        .addGap(287, 287, 287)
                        .addComponent(jLabel7))
                    .addGroup(ordersLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ordersLayout.createSequentialGroup()
                        .addGap(302, 302, 302)
                        .addComponent(jButton14)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        ordersLayout.setVerticalGroup(
            ordersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton14)
                .addContainerGap())
        );

        jPanel3.add(orders, "card3");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("User Details");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Price", "Category"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout userLayout = new javax.swing.GroupLayout(user);
        user.setLayout(userLayout);
        userLayout.setHorizontalGroup(
            userLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userLayout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        userLayout.setVerticalGroup(
            userLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel3.add(user, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 String v = validateFood(false);
    if (!v.equals("OK")) {
        JOptionPane.showMessageDialog(this, v, "Validation", JOptionPane.WARNING_MESSAGE);
        return;
    }

   String result = controller.updateMenuItem(
    jTextField1.getText().trim(),
    jTextField2.getText().trim(),
    String.valueOf(jComboBox1.getSelectedItem()),
    Double.parseDouble(jTextField3.getText().trim())
);


   
    if (!result.equals("OK")) {
        JOptionPane.showMessageDialog(this, result.replace("ERROR:", "").trim(),
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JOptionPane.showMessageDialog(this, "Food item updated!");
    loadTable();
    clearFoodFields();
    // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
String v = validateFood(true);
if (!v.equals("OK")) {
    JOptionPane.showMessageDialog(this, v, "Validation", JOptionPane.WARNING_MESSAGE);
    return;
}

String result = controller.addMenuItem(
        jTextField1.getText().trim(),
        jTextField2.getText().trim(),
        jComboBox1.getSelectedItem().toString(),
        getPriceValue()
);

if (!result.equals("OK")) {
    JOptionPane.showMessageDialog(this, result.replace("ERROR:", "").trim(),
            "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

JOptionPane.showMessageDialog(this, "Food item added!");
loadTable();
clearFoodFields();
menuList = store.getUsers();  // refresh list
refreshRecentFoods();         // refresh recent panel

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
 java.awt.CardLayout cl = (java.awt.CardLayout) jPanel3.getLayout();
    cl.show(jPanel3, "card4");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
java.awt.CardLayout cl = (java.awt.CardLayout) jPanel3.getLayout();
    cl.show(jPanel3, "card3");
    loadOrdersTable();       // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
java.awt.CardLayout cl = (java.awt.CardLayout) jPanel3.getLayout();
    cl.show(jPanel3, "card2");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 String id = jTextField1.getText().trim();

    if (id.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Food ID is required to delete.", "Validation",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int c = JOptionPane.showConfirmDialog(this,
            "Delete item ID: " + id + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

    if (c != JOptionPane.YES_OPTION) return;

    String result = controller.deleteMenuItem(id);

    if (!result.equals("OK")) {
        JOptionPane.showMessageDialog(this, result.replace("ERROR:", "").trim(),
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JOptionPane.showMessageDialog(this, "Deleted successfully!");
    loadTable();
    clearFoodFields();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to logout?",
        "Logout",
        JOptionPane.YES_NO_OPTION
);

if (confirm == JOptionPane.YES_OPTION) {
    new Login(store).setVisible(true);  
    this.dispose();
}
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
java.awt.CardLayout cl =
        (java.awt.CardLayout) jPanel3.getLayout();
    cl.show(jPanel3, "card5");        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
      selectionSortByName(); 
        loadTableFromMenuList();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
insertionSortByPrice();
    loadTableFromMenuList();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
String id = jTextField4.getText();

UserModel found = binarySearchById(id);

if (found == null) {
    JOptionPane.showMessageDialog(this, "Item ID not found");
} else {
    LinkedList<UserModel> one = new LinkedList<>();
    one.add(found);
    loadTableFromList(one);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String keyword = jTextField5.getText(); // your search field
java.util.LinkedList<UserModel> found = linearSearchMenu(keyword);
loadTableFromList(found);
// TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed
//Reset The search button
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
jTextField5.setText("");
jTextField4.setText("");
loadTableFromMenuList();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
int row = jTable2.getSelectedRow();
if (row == -1) {
    JOptionPane.showMessageDialog(this, "Select an order first.");
    return;
}

OrderModel done = controller.completeOrderAt(row);

if (done == null) {
    JOptionPane.showMessageDialog(this, "Could not complete order.");
    return;
}

loadOrdersTable();
loadOrderHistoryTable();

    }//GEN-LAST:event_jButton14ActionPerformed

    private void loadTable() {
    javax.swing.table.DefaultTableModel tm =
        (javax.swing.table.DefaultTableModel) jTable1.getModel();

    tm.setRowCount(0); // clear existing rows

    for (UserModel u : controller.getAllMenuItems()) {
        tm.addRow(new Object[]{
            u.getId(),
            u.getName(),
            u.getPrice(),
            u.getCategory()
        });
    }
}

    private void loadOrdersTable() {
    javax.swing.table.DefaultTableModel tm =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();

    tm.setRowCount(0);

    for (Model.OrderModel o : store.getAllOrders()) {
        tm.addRow(new Object[]{
            o.getUsername(),
            o.getItemId(),
            o.getItemName(),
            o.getQuantity(),
            o.getPrice(),
            o.getTotal()
        });
    }
}

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JPanel orders;
    private javax.swing.JPanel update;
    private javax.swing.JPanel user;
    // End of variables declaration//GEN-END:variables
}
