package main.models;

public enum NodeType {
    nd_None(""), nd_Ident("Identifier"), nd_Integer("Integer"),
    nd_int("int"),
    nd_Sequence("Sequence"), nd_If("If"),
    nd_Prti("Prti"), nd_Then("Then"), nd_Declaration("Declaration"),
    nd_Assign("Assign"), nd_Mul("Multiply"), nd_Add("Add"),
    nd_Bool("Boolean"), nd_Begin("Begin"), nd_End("End"),
    nd_While("While"), nd_do("Do"), nd_True("True"), nd_False("False"),
    nd_Gtr("Greater"), nd_Geq("GreaterEqual"), nd_Eql("Equal");
    private final String name;

    NodeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
