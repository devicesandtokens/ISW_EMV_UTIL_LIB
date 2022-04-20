package com.isw.pinencrypter

class XORorANDorOR {

    fun XORorANDorORfunction(valueA: String, valueB: String, symbol: String = "|"): String {
        val a = valueA.toCharArray()
        val b = valueB.toCharArray()
        var result = ""
        for (i in 0 until a.lastIndex + 1) {
            if (symbol === "|")
            {
                result +=
                    (Integer.parseInt(a[i].toString(),16).or (
                        Integer.parseInt(b[i].toString(),16)
                    ).toString(16).toUpperCase())
            } else if (symbol === "^")
            {
                result += (Integer.parseInt(a[i].toString(), 16)
                    .xor (Integer.parseInt(b[i].toString(),16)).toString(16).toUpperCase())
            }
            else { result += (Integer.parseInt(a[i].toString(), 16)
                .and (Integer.parseInt(b[i].toString(),16))).toString(16).toUpperCase()
            }
        }
        return result
    }
}