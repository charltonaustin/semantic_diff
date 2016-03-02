package com.charltonaustin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SemanticDiff {


    public HowDifferent itIsTheSame(String from, String to) {
        HashMap<MethodDeclaration, BlockStmt> fromMethods = new HashMap<MethodDeclaration, BlockStmt>();
        giveMeMethods(from, fromMethods);


        HashMap<MethodDeclaration, BlockStmt> toMethods = new HashMap<MethodDeclaration, BlockStmt>();
        giveMeMethods(to, toMethods);

        for (MethodDeclaration fromMethod : fromMethods.keySet()) {
            if (!toMethods.containsKey(fromMethod)) {
                return HowDifferent.DELETION;
            }
        }
        for (MethodDeclaration toMethod : toMethods.keySet()) {
            if (!fromMethods.containsKey(toMethod)) {
                return HowDifferent.ADDITION;
            }
            BlockStmt fromBody = fromMethods.get(toMethod);
            BlockStmt toBody = toMethods.get(toMethod);
            List<Statement> fromBodyStmts = fromBody.getStmts();
            List<Statement> toBodyStmts = toBody.getStmts();
            for (Statement toBodyStmt : toBodyStmts) {
                if (!(fromBodyStmts.contains(toBodyStmt))) {
                    return HowDifferent.ADDITION;
                }
            }
            for (Statement fromBodyStmt : fromBodyStmts) {
                if (!(toBodyStmts.contains(fromBodyStmt))) {
                    return HowDifferent.DELETION;
                }
            }

        }
        return HowDifferent.IDENTICAL;
    }

    private void giveMeMethods(String src, HashMap<MethodDeclaration, BlockStmt> myMethods) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(src.getBytes());

        try {
            CompilationUnit cu = JavaParser.parse(inputStream);

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

        HashMap<MethodDeclaration, BlockStmt> myMethods;

        public MethodVisitor(HashMap<MethodDeclaration, BlockStmt> myMethods) {
            this.myMethods = myMethods;
        }

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            BlockStmt body = n.getBody();
            n.setBody(new BlockStmt());
            myMethods.put(n, body);
            super.visit(n, arg);
        }
    }
}
