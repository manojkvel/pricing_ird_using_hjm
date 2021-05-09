Pricing Interest Rate Derivatives under HJM Model

Project Implementation

Technologies used
1. Apache Tomcat 6.0.3 as web server.
2. Java SDK 1.6
3. Google Web Tool kit for UI.
4. Java Servlets for Service layer

Libraries used
1. Universal Java Matrix Package (ujmp-complete-0.2.5) used for matrix operations and Jacobi orthogonal rotation.
2. Apache commons
a. Math (commons-math3-3.2) for Multiple Linear Regression.
b. File Upload (commons-fileupload-1.3) for CSV upload.
3. Gwt-servlet (gwt-servlet) for RPC.
4. JFreeChart(jfreechart-1.0.15) for PCA chart.

Process Flow
Data
1. Data has been collected from Bank of England. The data downloaded comprises of daily yield curve data.
2. The data is then formatted as per maturities starting from one month (0.08) till 25 years semi annually.
3. The data from 2005 to till date has been included.
4. The format that is understood by the application created in comma separated values or .csv format.
PCA
1. Once the user uploads the data in .csv format, the system calculates the daily differences.
2. A covariance matrix is created from the daily differences data.
3. The covariance matrix is then orthogonally rotated by the use of Jacobi technique.
4. The eigen vectors and eigen values are extracted post orthogonal rotation.
5. The eigen values are ranked and first three corresponding eigen vectors are selected as principal components.
6. The prominent Eigen Vectors or the principal components are then charted against tenors and the chart is saved to the Web server’s context path.
7. Then using the principal components , tenors and eigen values the volatility functions for the three components are estimated by curve fitting and the drift is calculated.
Monte Carlo Simulation
Once the PCA is done, the volatilities and drift are calibrated, the system simulates the forward rate differences based on the Musiela parametrization forward rate SDE for the number of Paths provided.
F+ for all all Tenors (0.08 to 25 – semi-annual) against the number of paths(steps) specified from the UI.
Pricing
Once the MC simulation is done; the user can go to the Pricing screen and get prices for Zero-coupon Bond and Caplet by specifying what maturity they are looking for in future.









Server Setup and Software Deployment

1. Apache Tomcat is being used as the web server for this application.
2. The binaries for the tomcat are provided along the project reports soft copy.
3. Please extract the apache tomcat file to your local drive.
4. Please make sure that you have set the JAVA_HOME environment variable to you java home directory.
5. Application uses Java 1.6 SDK.
6. Navigate to the bin directory of the tomcat installation folder.
7. Example: C:\apache\apache-tomcat-6.0.37\bin
8. Click on startup.bat(alternatively open up command prompt in windows, navigate to C:\apache\apache-tomcat-6.0.37\bin and type startup and press Enter.
9. This action should trigger the start of tomcat web server.
10. Now go to the deployment folder of tomcat. Navigate to webapps dir ., example C:\apache\apache-tomcat-6.0.37\webapps
11. Copy the CQFPortal.war given in the pen drive and paste it in the webapps directory of tomcat.
12. In a few seconds the tomcat deploys the web application archieve(war) as exploded form in the webapps directory with a name CQFPortal.
13. Now the app is ready to use.
14. Open up a browser and type the url http://localhost:8080/CQFPortal.
15. Note: If you are using your own tomcat server and in case of any deployment failure , then check if the invokerServlet is active and privileged in your tomcat config.
16. For further reference please go through http://www.coreservlets.com/Apache-Tomcat-Tutorial/detailed-configuration.html.
