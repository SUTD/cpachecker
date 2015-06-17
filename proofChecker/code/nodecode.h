
/* encoding of tree node types and symbols */

#ifndef NODECODE_H
#define NODECODE_H

extern int LHSMAP[];

#define IsSymb(_n,_c) (LHSMAP[(_n)->_prod] == (_c))

#define SYMBunary_operator 0
#define SYMBshift_operator 1
#define SYMBsource 2
#define SYMBroot 3
#define SYMBparameter 4
#define SYMBparameter_list 5
#define SYMBinitializer 6
#define SYMBinit_declarator 7
#define SYMBinclusive_OR_operator 8
#define SYMBtranslation_unit 9
#define SYMBfile 10
#define SYMBpre_include 11
#define SYMBexternal_declaration 12
#define SYMBexclusive_OR_operator 13
#define SYMBinit_declarator_list 14
#define SYMBdeclaration 15
#define SYMBinteger_constant 16
#define SYMBconstant 17
#define SYMBargument_expression_list 18
#define SYMBand_operator 19
#define SYMBBinOp 20
#define SYMBargument_expression_list_opt 21
#define SYMBExpression 22
#define SYMBfunction_call_expression 23
#define SYMBassignment_operator 24
#define SYMBIdUse 25
#define SYMBassignment_expression 26
#define SYMBexpression_opt 27
#define SYMBselection_statement 28
#define SYMBexpression_statement 29
#define SYMBlabeled_statement 30
#define SYMBstatement 31
#define SYMBstatement_list 32
#define SYMBstatement_list_opt 33
#define SYMBdeclaration_list 34
#define SYMBcompound_statement 35
#define SYMBjump_statement 36
#define SYMBcompound_statement_opt 37
#define SYMBparameter_list_opt 38
#define SYMBtype_specifier 39
#define SYMBfunction_definition 40
#define RULErule_26 0
#define RULErule_25 1
#define RULErule_24 2
#define RULErule_23 3
#define RULErule_22 4
#define RULErule_21 5
#define RULErule_20 6
#define RULErule_19 7
#define RULErule_18 8
#define RULErule_17 9
#define RULErule_16 10
#define RULErule_15 11
#define RULErule_14 12
#define RULErule_13 13
#define RULErule_7 14
#define RULErule_6 15
#define RULErule_5 16
#define RULErule_4 17
#define RULErule_29 18
#define RULErule_30 19
#define RULErule_31 20
#define RULErule_32 21
#define RULErule_33 22
#define RULErule_34 23
#define RULErule_35 24
#define RULErule_36 25
#define RULErule_37 26
#define RULErule_38 27
#define RULErule_39 28
#define RULErule_40 29
#define RULErule_41 30
#define RULErule_42 31
#define RULErule_43 32
#define RULErule_44 33
#define RULErule_45 34
#define RULErule_46 35
#define RULErule_47 36
#define RULErule_48 37
#define RULErule_49 38
#define RULErule_50 39
#define RULErule_51 40
#define RULErule_52 41
#define RULErule_53 42
#define RULErule_54 43
#define RULErule_55 44
#define RULErule_56 45
#define RULErule_57 46
#define RULErule_58 47
#define RULErule_59 48
#define RULErule_60 49
#define RULErule_61 50
#define RULErule_62 51
#define RULErule_63 52
#define RULErule_64 53
#define RULErule_65 54
#define RULErule_66 55
#define RULErule_67 56
#define RULErule_68 57
#define RULErule_69 58
#define RULErule_70 59
#define RULErule_71 60
#define RULErule_72 61
#define RULErule_73 62
#define RULErule_74 63
#define RULErule_75 64
#define RULErule_76 65
#define RULErule_77 66
#define RULErule_78 67
#define RULErule_79 68
#define RULErule_80 69
#define RULErule_81 70
#define RULErule_82 71
#define RULErule_83 72
#define RULErule_84 73
#define RULErule_85 74
#define RULErule_86 75
#define RULErule_87 76
#define RULErule_88 77
#define RULErule_89 78
#define RULErule_90 79
#define RULErule_91 80
#define RULErule_92 81
#define RULErule_93 82
#define RULErule_94 83
#define RULErule_95 84
#define RULErule_96 85
#define RULErule_97 86
#define RULErule_98 87
#define RULErule_99 88
#define RULErule_100 89
#define RULErule_101 90
#define RULErule_102 91
#define RULErule_103 92
#define RULErule_104 93
#define RULErule_105 94
#define RULErule_106 95
#define RULErule_107 96
#define RULErule_28 97
#define RULErule_27 98
#define RULErule_12 99
#define RULErule_11 100
#define RULErule_10 101
#define RULErule_9 102
#define RULErule_8 103
#define RULErule_3 104
#define RULErule_2 105
#define RULErule_1 106
#endif