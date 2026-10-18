package com.Adityabhosale;


import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class EmployeeMenu
{
    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);
        int choice;

        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        cfg.addAnnotatedClass(Employee.class);

        try (SessionFactory factory = cfg.buildSessionFactory();) {


            do {

                System.out.println("\n======Employee Menu =====");
                System.out.println("1️⃣ Insert");
                System.out.println("2️⃣ Delete");
                System.out.println("3️⃣ Update");
                System.out.println("4️⃣ Search");
                System.out.println("5️⃣ View All ");
                System.out.println("6️⃣ Exit");
                System.out.println("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1: {
                        System.out.println("Enter Employee ID: ");
                        int id = sc.nextInt();

                        System.out.println("Enter Employee Name: ");
                        String name = sc.next();

                        System.out.println("Enter Employee Salary: ");
                        float salary = sc.nextFloat();

                        Employee employee = new Employee(id,name,salary);
                        Transaction tx = null;

                        try(Session session = factory.openSession()) {
                            tx = session.beginTransaction();
                            session.persist(employee);
                            tx.commit();
                            System.out.println("✅✅✅ Employee inserted successfully: ");


                        } catch(ConstraintViolationException cve) {
                            if (tx != null) {
                                tx.rollback();
                            }
                            System.err.println("⚠️⚠️⚠️ Employee Already Exist "+ cve.getMessage());

                        }catch (TransactionException te) {
                            if(tx != null) {
                                tx.rollback();
                            }
                            System.err.println("⚠️⚠️⚠️ Transaction Error during insertion "+ te.getMessage());

                        }catch (HibernateException he){
                            if(tx != null) {
                                tx.rollback();
                            }
                            System.err.println("⚠️⚠️⚠️ Hibernate Error during insertion "+ he.getMessage());

                        }catch (Exception e){
                            if(tx != null) {
                                tx.rollback();
                            }
                                System.err.println("⚠️⚠️⚠️ General problem during insertion "+ e.getMessage());
                        }

                    }
                    break;
                    case 2: {
                        System.out.println("Enter Delete Employee ID: ");
                        int id = sc.nextInt();

                        Transaction tx = null;
                        try (Session session = factory.openSession()) {
                            Employee employee = session.find(Employee.class, id);

                            tx = session.beginTransaction();
                            if (employee != null) {
                                session.remove(employee);
                                tx.commit();
                                System.out.println("✅✅✅ Deleted Employee ID Successfully");

                            }else  {
                                System.out.println("⚠️⚠️⚠️ Employee ID not found");
                            }


                        } catch (Exception e) {
                            if (tx != null) {
                                tx.rollback();
                            }
                            System.err.println("⚠️⚠️⚠️ Delete Failure "+e.getMessage());
                        }

                    }
                    break;
                    case 3: {
                        System.out.println("Enter Employee ID To be Updated: ");
                        int id = sc.nextInt();


                        Transaction tx = null;
                        try(Session session = factory.openSession()) {
                            Employee employee = session.find(Employee.class, id);

                            tx = session.beginTransaction();
                            if(employee != null) {
                                System.out.println("1️⃣ Enter Update Name\n2️⃣ Update Salary ");
                                System.out.println("Enter your choice: ");
                                int choice1 = sc.nextInt();
                                switch (choice1) {
                                    case 1: {
                                        System.out.println("Enter New Name: ");
                                        String name = sc.next();
                                        employee.setName(name);
                                    }
                                    break;
                                    case 2: {
                                        System.out.println("Enter New Salary: ");
                                        float salary = sc.nextFloat();
                                        employee.setSalary(salary);
                                    }
                                    break;
                                    default:
                                        System.out.println("⚠️⚠️⚠️ Wrong choice");
                                }
                                session.merge(employee);
                                tx.commit();
                                System.out.println("✅✅✅ Employee Updated successfully");
                            }else {
                                System.out.println("⚠️⚠️⚠️ Employee not found");
                            }


                        } catch (Exception e){
                            if(tx != null) {
                                tx.rollback();
                            }
                            System.err.println("⚠️⚠️⚠️ Update Failed "+e.getMessage());
                        }

                    }
                    break;
                    case 4: {
                        System.out.println(" Enter Search Employee ID :");
                        int id = sc.nextInt();

                        try (Session session = factory.openSession()) {
                            Employee employee = session.find(Employee.class, id);
                            if(employee != null){
                                System.out.println("ID\t\tNAME\t\tSALARY");
                                System.out.println(employee);


                            }else {
                                System.out.println("⚠️⚠️⚠️ Employee not found");
                            }



                        }catch (Exception e) {
                            System.err.println("⚠️⚠️⚠️ Search Failed! "+ e.getMessage());
                        }

                    }
                    break;
                    case 5: {
                        try(Session session = factory.openSession()) {
                           List<Employee> list = session.createQuery("from Employee", Employee.class).getResultList();

                           if (list.isEmpty()) {
                               System.out.println("⚠️⚠️⚠️ Employee not found");
                           }else  {
                               System.out.println("ID\t\tNAME\t\tSALARY");
//                               for (Employee employee : list) {
//                                   System.out.println(employee);

                               list.forEach(System.out::println);
                               }



                        } catch (Exception e) {
                            System.err.println("View Failed: " +e.getMessage());
                        }

                    }
                    break;
                    case 6: {
                        System.out.println("🙋‍♂️🙋‍♂️🙋‍♂️🙋‍♂️ Exiting...");
                    }break;
                    default:
                        System.out.println("⚠️ Invalid Choice ");
                }

            } while (choice != 6);

        } catch (JDBCConnectionException jce) {
            System.err.println("❌❌❌ Connection Failed! Check output console: " + jce.getMessage());

        } catch (HibernateException he) {
            System.err.println("❌❌❌ Hibernate Initialization Error: " + he.getMessage());

        }catch (
    Exception e) {
            System.err.println("❌❌❌ General Error: " + e.getMessage());

        } finally {
            sc.close();
        }
    }
}
