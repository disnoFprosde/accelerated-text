resource IsAEst = open ParadigmsEst, SyntaxEst, UtilsEst, BaseDictionaryEst in {

  oper -- T1000 is a kettle

    isA_S : CN -> CN -> S =
      \subject,attribute ->
      (mkS
         (mkCl (mkNP subject)
            (mkNP a_Det attribute))) ;

}
