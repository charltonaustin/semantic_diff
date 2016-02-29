package com.charltonaustin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class SemanticDiff {


    public HowDifferent itIsTheSame(String from, String to) {
        HashMap<Integer, Node> myMethods = new HashMap<Integer, Node>();

        giveMeMethods(from, myMethods);

        HashMap<Integer, Node> myOtherMethods = new HashMap<Integer, Node>();

        giveMeMethods(to, myOtherMethods);
        for (int method : myMethods.keySet()) {
            if(!myOtherMethods.containsKey(method)){
                return HowDifferent.NOT_IDENTICAL;
            }
        }
        for (int method : myOtherMethods.keySet()) {
            if(!myMethods.containsKey(method)){
                return HowDifferent.NOT_IDENTICAL;
            }
        }
        return HowDifferent.IDENTICAL;
    }

    private void giveMeMethods(String src, HashMap<Integer, Node> myMethods) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(src.getBytes());

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(inputStream);
            MethodVisitor methodVisitor = new MethodVisitor(myMethods);
            methodVisitor.visit(cu, null);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        HashMap<Integer, Node> myMethods;

        public MethodVisitor(HashMap<Integer, Node> myMethods) {
            this.myMethods = myMethods;
        }

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            myMethods.put(n.hashCode(), n );
            super.visit(n, arg);
        }
    }
}
