/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning.util;

/**
 *
 * @author alexhuleatt
 */
public class Pair<A,B> {
    public A a;
    public B b;
    
    public Pair(A a, B b) {
        this.a=a;
        this.b=b;
    }
    
    public String toString() {
        return a.toString() +", " + b.toString();
    }
}
