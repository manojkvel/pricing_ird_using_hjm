/**
 * 
 */
package cqfportal.server;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 * @author Workstation
 *
 */
public class JacobiServiceImpl {

	
public Matrix getEigenVectors(double[][] Amat,double atol){
	//   Uses the Jacobi method to get the eigenvectors for a symmetric matrix
	//   Similar to eigenvalue function, but with additional V matrix updated with each rotation
	//   Uses MatrixUTSumSq fn
	//   Uses JacobiAmat fn
	//   Uses JacobiVmat fn
	//   Uses MatrixIdentity fn
	    
	    double asumsq;
	    int n,r;
	    //Dim n As Integer, r As Integer
	    double[][] Vmat,Anext;
	    Matrix Vnext=null;
	    
	    
	    //Dim Anext As Variant, Vmat As Variant, Vnext As Variant
	    
	    n = Amat[0].length;
	    r = 0;
	    Vmat = getIdentityMatrix(n).toDoubleArray();
	   //PCAComponent.print(getIdentityMatrix(n));
	    asumsq = MatrixUTSumSq(Amat);
	    
	   // //System.out.println(asumsq);
	    while(asumsq>atol){
	    	////System.out.println(asumsq);
	    	Anext=JacobiAmat(n, Amat);
	    	Vnext=JacobiVmat(n, Amat, Vmat);
	    	asumsq= MatrixUTSumSq(Anext);
	    	Amat= Anext;
	    	Vmat=Vnext.toDoubleArray();
	    	r++;
	    }
	    return Vnext;
//	    Do While asumsq > atol 'Calculation continues until the sum of squares of off-diagonal elements is below the set tolerance value - this is a numerical method
//	        Anext = JacobiAmat(n, Amat) 'Rotation
//	        Vnext = JacobiVmat(n, Amat, Vmat) 'History of rotation matrices P is kept to calculate eigenvector matrix V= I x P1 x P2...= Vi x Pi, where V0=I
//	        asumsq = MatrixUTSumSq(Anext)
//	        Amat = Anext
//	        Vmat = Vnext
//	        r = r + 1
//	    Loop
//	    EigenvectorsEmat = Vnext
	    
}


public Matrix getIdentityMatrix(int n){
	//   Returns the (nxn) Identity Matrix
	    int i;
	    double[][] Imat = new double[n][n];
	    for(i=0;i<n;i++)
	        Imat[i][i] = 1;
	   return MatrixFactory.importFromArray(Imat); 
}


public double MatrixUTSumSq(double[][] Xmat){
//   Returns the Sum of Squares of the Upper Triangle of a Matrix
    double sum;
    int i , j, n;
    n = Xmat[0].length;
    ////System.out.println(n);
    sum = 0;
    for(i=0;i<n;i++){
    	for(j=i+1;j<n;j++){
    		sum=sum+(Xmat[i][j]*Xmat[i][j]);
    	}
    }
   return sum;
}

public double[][] JacobiAmat(int n, double[][] Athis){
//   Returns Anext matrix, updated using the P rotation matrix
//   Uses Jacobirvec fn
//   Uses JacobiPmat fn
	double[] rthis;
Matrix Aathis,Pthis,Anext;
rthis = Jacobirvec(n,Athis);
Pthis = JacobiPmat(n, rthis);
Aathis = MatrixFactory.importFromArray(Athis);
Anext = Pthis.transpose().mtimes(Aathis.mtimes(Pthis));
return Anext.toDoubleArray();

}


public double[] Jacobirvec(int n, double[][] Athis){
//   Returns vector containing mr, mc and jrad
//   These are the row and column vectors and the angle of rotation for the P matrix
    double maxval, jrad;
    int i, j, mr, mc;
    double[][] Awork= new double[n][n];
    maxval = -1;
    mr = -1;
    mc = -1;
//   Two cycles below represent the search for the largest off-diagonal element in the matrix Athis
//   To elimiate this element, the appropraite roatation angle is calculated
    for(i=0;i<n;i++){
    	for(j=i+1;j<n;j++){
    		Awork[i][j]=Math.abs(Athis[i][j]);
    		if(Awork[i][j] > maxval){
    			maxval = Awork[i][j];
    			mr=i;
    			mc=j;
    		}
    	}
    }
    if(Athis[mr][mr] ==Athis[mc][mc]){
    	jrad = 0.25 * Math.PI * Math.signum(Athis[mr][mc]);
    	
    }else{
    	jrad=0.5*Math.atan(2*Athis[mr][mc]/(Athis[mr][mr]-Athis[mc][mc]));
    }
   // //System.out.println(mr+","+mc+","+jrad);
    return new double[]{mr,mc,jrad};
}

public Matrix JacobiPmat(int n, double[] rthis){
//   Returns the rotation Pthis matrix
//   Uses MatrixIdentity fn
//   Uses Jacobirvec fn
    double[][] Pthis;
    int mr = Double.valueOf(rthis[0]).intValue();
    int mc = Double.valueOf(rthis[1]).intValue();
    Pthis = getIdentityMatrix(n).toDoubleArray();
    Pthis[mr][mr] = Math.cos(rthis[2]);
    Pthis[mc][mr] = Math.sin(rthis[2]);
    Pthis[mr][mc] = -Math.sin(rthis[2]);
    Pthis[mc][mc] = Math.cos(rthis[2]);
   return MatrixFactory.importFromArray(Pthis);
}

public Matrix JacobiVmat(int n, double[][]Athis, double[][]Vthis){
//   Returns Vnext matrix
//   Keeps track of the eigenvectors during the rotations
//   Uses Jacobirvec fn
//   Uses JacobiPmat fn
	double[] rthis;
	Matrix Pthis;
	Matrix Vnext;
    rthis = Jacobirvec(n, Athis); //Search for the largest off-diagonal element to be eliminated by rotation, generate the angle of rotation
    Pthis = JacobiPmat(n, rthis); //Generate rotation matrix P
    Vnext = MatrixFactory.importFromArray(Vthis).mtimes(Pthis); //Improving eigenvectors by Vi x Pi, where V reflects previous multipliations
    return Vnext;
}

public double[] Eigenvaluesevec(double[][] Amat, double atol){
//   Uses the Jacobi method to get the eigenvalues for a symmetric matrix
//   Amat is rotated (using the P matrix) until its off-diagonal elements are minimal
//   Uses MatrixUTSumSq fn
//   Uses JacobiAmat fn
//    Application.Volatile (False)
    double asumsq;
    int i, n, r;
    double[][] Anext;
    n = Amat[0].length;
    double[] evec = new double[n];
    		
    r = 0;
    asumsq = MatrixUTSumSq(Amat);
    while(asumsq>atol){
    	Anext=JacobiAmat(n, Amat);
    	asumsq= MatrixUTSumSq(Anext);
    	Amat= Anext;
    	r++;
    }
    for(i=0;i<n;i++){
    	evec[i] = Amat[i][i];
    }
    return evec;
}
 

}
/*
Option Explicit
Option Base 1



Function MatrixTrace(Xmat)
'   Returns the trace of a matrix (sum of elements on leading diagonal)
    Dim sum
    Dim i As Integer, n As Integer
    n = Xmat.Columns.Count
    sum = 0
    For i = 1 To n
        sum = sum + Xmat(i, i)
    Next i
    MatrixTrace = sum
End Function
    








Function Eigenvaluesevec(Amat, atol)
'   Uses the Jacobi method to get the eigenvalues for a symmetric matrix
'   Amat is rotated (using the P matrix) until its off-diagonal elements are minimal
'   Uses MatrixUTSumSq fn
'   Uses JacobiAmat fn
    Application.Volatile (False)
    Dim asumsq
    Dim i As Integer, n As Integer, r As Integer
    Dim evec() As Variant
    Dim Anext As Variant
    n = Sqr(Application.Count(Amat))
    r = 0
    asumsq = MatrixUTSumSq(Amat)
    Do While asumsq > atol
        Anext = JacobiAmat(n, Amat)
        asumsq = MatrixUTSumSq(Anext)
        Amat = Anext
        r = r + 1
    Loop
    ReDim evec(n) ' evec() has vectorised eigenvalues
    For i = 1 To n ' this procedure moves eigenvalues from the diagonal matrix Amat() into the vector evec() for projection
        evec(i) = Amat(i, i)
    Next i
    Eigenvaluesevec = evec
End Function



Function EigenvectorsEmat(Amat, atol)
'   Uses the Jacobi method to get the eigenvectors for a symmetric matrix
'   Similar to eigenvalue function, but with additional V matrix updated with each rotation
'   Uses MatrixUTSumSq fn
'   Uses JacobiAmat fn
'   Uses JacobiVmat fn
'   Uses MatrixIdentity fn
    Application.Volatile (False)
    Dim asumsq
    Dim n As Integer, r As Integer
    Dim Anext As Variant, Vmat As Variant, Vnext As Variant
    n = Sqr(Application.Count(Amat))
    r = 0
    Vmat = MatrixIdentity(n)
    asumsq = MatrixUTSumSq(Amat)
    Do While asumsq > atol 'Calculation continues until the sum of squares of off-diagonal elements is below the set tolerance value - this is a numerical method
        Anext = JacobiAmat(n, Amat) 'Rotation
        Vnext = JacobiVmat(n, Amat, Vmat) 'History of rotation matrices P is kept to calculate eigenvector matrix V= I x P1 x P2...= Vi x Pi, where V0=I
        asumsq = MatrixUTSumSq(Anext)
        Amat = Anext
        Vmat = Vnext
        r = r + 1
    Loop
    EigenvectorsEmat = Vnext
End Function

Function PDcheck(evec)
'   Checks definiteness of symmetric matrices using their eigenvalues
'   Returns 1 (+ve def), 0.5 (+ve semi-def), -0.5 (-ve semi-def), -1 (-ve def)
    Dim pd, sa, smin, smax
    Dim i As Integer, n As Integer, p As Integer
    Dim svec() As Variant
    n = Application.Count(evec)
    ReDim svec(n)
    For i = 1 To n
        svec(i) = Sgn(evec(i))
    Next i
    sa = Application.sum(svec)
    smin = Application.Min(svec)
    smax = Application.Max(svec)
    If sa = n Then
        pd = 1
    ElseIf sa = -n Then
        pd = -1
    ElseIf sa >= 0 And smin >= 0 Then
        pd = 0.5
    ElseIf sa <= 0 And smax <= 0 Then
        pd = -0.5
   Else
        p = 0
    End If
    PDcheck = pd
End Function

*/
