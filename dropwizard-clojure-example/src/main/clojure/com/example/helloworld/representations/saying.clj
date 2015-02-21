(ns com.example.helloworld.representations.saying)

(defn saying [id content]
  {:pre [(<= (count content) 10)]}
  {"id" id "content" content})
