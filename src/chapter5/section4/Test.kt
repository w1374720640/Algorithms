package chapter5.section4

fun testNFA(create: (String) -> NFA) {
    // 基础连接、或、闭包测试
    val nfa1 = create("((A*B|AC)D)")
    check(nfa1.recognizes("AAABD"))
    check(nfa1.recognizes("BD"))
    check(nfa1.recognizes("ACD"))
    check(!nfa1.recognizes("AACD"))
    check(!nfa1.recognizes("ABBD"))
    check(!nfa1.recognizes("AAAB"))
    check(!nfa1.recognizes("AAABDAB"))

    // 嵌套基础测试
    val nfa2 = create("(.*AB((C|D*E)F)*G)")
    check(nfa2.recognizes("ABG"))
    check(nfa2.recognizes("CCABG"))
    check(nfa2.recognizes("ABABCFG"))
    check(nfa2.recognizes("ACDDABDDDEFCFEFG"))
    check(nfa2.recognizes("AAABCFCFDEFCFDDDEFG"))
    check(!nfa2.recognizes("AAABCG"))
    check(!nfa2.recognizes("ABDEFCEFCG"))
    check(!nfa2.recognizes("ABBCFG"))
    check(!nfa2.recognizes("ABABAB"))
    check(!nfa2.recognizes("ABABFG"))
    check(!nfa2.recognizes("ABGFC"))

    // 闭包 + ? * 测试，多向或测试
    val nfa3 = create("(AB+.?D|EF*|(GH+)?I)+")
    check(nfa3.recognizes("ABD"))
    check(nfa3.recognizes("ABCD"))
    check(nfa3.recognizes("ABBBED"))
    check(nfa3.recognizes("E"))
    check(nfa3.recognizes("EFFFE"))
    check(nfa3.recognizes("EFFEFEFEFF"))
    check(nfa3.recognizes("I"))
    check(nfa3.recognizes("GHI"))
    check(nfa3.recognizes("GHHHHI"))
    check(nfa3.recognizes("EFFFABBCDIGHHI"))
    check(!nfa3.recognizes(""))
    check(!nfa3.recognizes("ACD"))
    check(!nfa3.recognizes("ABCCD"))
    check(!nfa3.recognizes("GGHHI"))
    check(!nfa3.recognizes("GHGHI"))

    // 指定集合测试
    val nfa4 = create("(A[BE-I]*|A[^CF-H]+K|EEC[FXM]C)")
    check(nfa4.recognizes("A"))
    check(nfa4.recognizes("ABBEFIIG"))
    check(nfa4.recognizes("ABDDK"))
    check(nfa4.recognizes("EECXC"))
    check(!nfa4.recognizes("ABBCD"))
    check(!nfa4.recognizes("AK"))
    check(!nfa4.recognizes("ACK"))
    check(!nfa4.recognizes("AGK"))
    check(!nfa4.recognizes("AXXXCK"))
    check(!nfa4.recognizes("EECC"))
    check(!nfa4.recognizes("EECHC"))

    // 转义字符测试
    val nfa5 = create("(A\\.*E|CD[E\\-H]+|CF\\\\A)")
    check(nfa5.recognizes("AE"))
    check(nfa5.recognizes("A.E"))
    check(nfa5.recognizes("A...E"))
    check(nfa5.recognizes("CDEEE"))
    check(nfa5.recognizes("CD---H"))
    check(nfa5.recognizes("CDEH-"))
    check(nfa5.recognizes("CF\\A"))
    check(!nfa5.recognizes("ABE"))
    check(!nfa5.recognizes("CD"))
    check(!nfa5.recognizes("CDF"))
    check(!nfa5.recognizes("CD--CD-"))
    check(!nfa5.recognizes("CFA"))
    check(!nfa5.recognizes("CF\\\\A"))

    // 综合测试
    val nfa6 = create("((A.?D)+[DH-KY]|CD\\+*(EF?|GH+|CC\\*)\\++|CF[^DE\\-H]?[\\^D\\(F-I])")
    check(nfa6.recognizes("ADD"))
    check(nfa6.recognizes("ABDI"))
    check(nfa6.recognizes("ACDADI"))
    check(nfa6.recognizes("CDE+"))
    check(nfa6.recognizes("CD+++GH++++"))
    check(nfa6.recognizes("CD+++CC*++++"))
    check(nfa6.recognizes("CFD"))
    check(nfa6.recognizes("CFFG"))
    check(nfa6.recognizes("CF("))
    check(nfa6.recognizes("CFFG"))
    check(nfa6.recognizes("CFF^"))
    check(!nfa6.recognizes("ACCDE"))
    check(!nfa6.recognizes("ACDADABD"))
    check(!nfa6.recognizes("CDDEF+"))
    check(!nfa6.recognizes("CD+++CCCC++"))
    check(!nfa6.recognizes("CFDE"))
    check(!nfa6.recognizes("CFF-"))
    check(!nfa6.recognizes("CFFE"))
    check(!nfa6.recognizes("CF--"))

    println("NFA check succeed.")
}