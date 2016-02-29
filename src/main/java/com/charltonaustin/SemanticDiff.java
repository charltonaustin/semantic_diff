package com.charltonaustin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SemanticDiff {


    public boolean itIsTheSame(String from, String to) {
        HashSet<String> myMethods = new HashSet<String>();

        giveMeMethods(from, myMethods);

        HashSet<String> myOtherMethods = new HashSet<String>();

        giveMeMethods(to, myOtherMethods);
        for (String method : myMethods) {
            if(!myOtherMethods.contains(method)){
                return false;
            }
        }
        for (String method : myOtherMethods) {
            if(!myMethods.contains(method)){
                return false;
            }
        }
        return true;
    }

    private void giveMeMethods(String src, HashSet<String> myMethods) {
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

        Set<String> myMethods;

        public MethodVisitor(Set<String> myMethods) {
            this.myMethods = myMethods;
        }

        @Override
        public void visit(MethodDeclaration n, Object arg) {

            String signature = n.getName();
            signature = signature + " " + n.getParameters();
            myMethods.add(signature );
            super.visit(n, arg);
        }
    }
}
