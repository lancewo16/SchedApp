# SchedApp

Title: C195 Global Consulting - Scheduling Desktop Application
Purpose: To give functional management of customers' records and their appointments for Global Consulting Inc.
Author: 
Contact Info: 

Application Version: 1.0
Date of build: 1 December 2020

IDE: IntelliJ IDEA 2020.3 (Community Edition)
JAVA SE: Coretto- 11.0.6.10.1
JAVAFX SDK: 11.0.2

LOGIN/MAIN MENU

Description: After providing valid login information the user has access to the Main Menu. Here, the user can access other pages to view/edit customer records, customer appointments, and view reports on these.
*NOTE* All login attempts are logged to the file login_activity.txt. Info is verified with the database.

CUSTOMER MAIN

 Description: User is able to view a list of customers and pertinent information in a Table View. Once user has selected a customers, they can now add new customers, edit current customers, or delete the currently
 selected customers.
*NOTE*Deleting a customers will delete all associated appointments with that customer
*NOTE* Customer ID's are preset and cannot be edited. The "Created By" and "Last Updated By" text fields will prepopulate with the user who logged in and can then be modified

APPOINTMENT MAIN

Description: This page works in the same way as the CUSTOMER MAIN section, allowing the adding, editing, and deleting of appointments.
*NOTE* On the EDIT APPOINTMENT view a drop down with a Contact Name will populate the Contact ID field.

USER VIEW

Description: All appointments are shown in a table view. Above the table are two radio buttons which filter appointments by the week or the monthly

REPORTS
1. Customer Appointments: This is a report of the number of customers appointments per month and the type of appointment

2. Contact Schedules - Directions: From the dropdown menu the user selects a contact and view their assigned appointments in the tableview.
Description: This shows the schedule for each contact in the organization, and includes: appointment ID, title, type and description, start date and time, end date and time, and customer ID

3. Customer Schedules - Directions: From the dropdown menu, the customer selects a customers and views their assigned appointments in the tableview.
Description: This shows the schedule for each contact in the organization, and includes: appointment ID, title, type and description, start date and time, end date and time, and customer ID
