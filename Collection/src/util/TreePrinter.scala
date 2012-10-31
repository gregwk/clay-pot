package util

trait TreePrinter[A] {
    
    def prettyPrint(): String
    def prettyPrintKeys(): String
    def prettyPrintKeys(key: A, index: Int): String
}