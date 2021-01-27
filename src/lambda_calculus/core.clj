(ns lambda-calculus.core)

; vamos escrever todo o código usando apenas funções anonimas de 1 argumento

; numerais de church

(def zero
  (fn [f]
    (fn [x]
      x)))

(def succ (fn [n]
           (fn [f]
             (fn [x]
               (f ((n f) x))))))

(comment (succ zero))

(def one
  (fn [f]
    (fn [x]
      (f x))))

(def two
  (fn [f]
    (fn [x]
      (f (f x)))))

(def add
  (fn [a]
    (fn [b]
      (fn [f]
        (fn [x]
          ((a f) ((b f) x)))))))

(def three ((add two) one))

(def four ((add two) two))

(def seven ((add four) three))
(comment ((three (fn [x] (println x) x)) "AE!"))
(comment ((seven (fn [x] (println x) x)) "AE!"))

(def church->int
  (fn [f]
    ((f (fn [x]
          (+ x 1))) 0)))
(comment (church->int seven))
(comment (church->int (succ zero)))

(def mult
  (fn [m]
    (fn [n]
      (fn [f]
        (fn [x]
          ((m (n f)) x))))))

(comment (church->int ((mult three) four)))
(comment ((((mult three) four) (fn [x] (println x) x)) "AE!"))

(def expt
  (fn [base]
    (fn [expoente]
      (expoente base))))
(comment (church->int ((expt two) three)))

;booleanos e operadores lógicos

;boolean true
(def t
  (fn [a]
    (fn [b]
      a)))

;boolean false
(def f
  (fn [a]
    (fn [b]
      b)))

(def -not
  (fn [x]
    ((x f) t)))

(comment (print "hue"))

;implementação de um 'if'
(def -cond
  (fn [conditional]
    (fn [expr1]
      (fn [expr2]
        ((conditional expr1) expr2)))))


(def -and
  (fn [x]
    (fn [y]
      ((x y) f))))

(def -or
  (fn [x]
    (fn [y]
      ((x t) y))))

(comment 
  (= f (-not t))
  (= t (-not f))

  (= t (((-cond t) t) f))
  (= f (((-cond f) t) f))

  (((-cond t) :true) :false)
  (((-cond f) :true) :false)
  ((((-cond t) (fn [] (prn "VDD"))) (fn [] (prn "MIDIRA"))))

  (= t ((-and t) t))
  (= f ((-and t) f))
  (= f ((-and f) t))
  (= f ((-and f) f))

  (= t ((-or t) t))
  (= t ((-or t) f))
  (= t ((-or f) t))
  (= f ((-or f) f))
  )


;listas em calculo lambda
(def -cons
  (fn [a]
    (fn [b]
      (fn [f]
        ((f a) b)))))

(def -car
  (fn [x]
    (x (fn [a]
         (fn [b]
           a)))))
(def -cdr
  (fn [x]
    (x (fn [a]
         (fn [b]
           b)))))

(def lista ((-cons 1) ((-cons 2) 3)))
(comment 
  (-car lista)
  (-car (-cdr lista))
  (-cdr lista)
  (-cdr (-cdr lista))
  )


;combinador Y (para recursão)
(def Y (fn [f]
         ((fn [x]
            (x x))
          (fn [x]
            (f (fn [y]
                 ((x x) y)))))))

(def fact (fn [func]
            (fn [n]
              (if (zero? n)
                1
                (* n (func (dec n)))))))

(comment ((Y fact) 10)
         )

(comment (((fn [f]
             ((fn [x]
                (x x))
              (fn [x]
                (f (fn [y]
                     ((x x) y)))))) (fn [func]
                                      (fn [n]
                                        (if (zero? n)
                                          1
                                          (* n (func (dec n))))))) 5))
