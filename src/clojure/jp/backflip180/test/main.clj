(ns jp.backflip180.test.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]]
        [neko.log :only [deflog]]
        [neko.ui.adapters :only [ref-adapter]]
        [neko.application :only [defapplication]])
  (:import android.widget.TextView
           android.widget.Toast
           android.content.Intent))

(deflog "test_app")

(gen-class
 :name jp.backflip180.test.DetailActivity
 :extends android.app.Activity
 :exposes-methods {onCreate superOnCreate}
 :prefix detail-)

(defn detail-onCreate [this #^android.os.Bundle bundle]
  (.superOnCreate this bundle)
  (.setContentView this jp.backflip180.test.R$layout/detail)
  (log-d (str (.getIntExtra (.getIntent this) "ID" 0))))

(gen-class
 :name jp.backflip180.test.MainActivity
 :extends android.app.Activity
 :exposes-methods {onCreate superOnCreate})

(defn -onCreate [this #^android.os.Bundle bundle]
  (.superOnCreate this bundle)
  (.setContentView this jp.backflip180.test.R$layout/main)
  (doseq [id [1 2 3]]
    (.setOnClickListener
     (. this
        findViewById
        (-> (str "jp.backflip180.test.R$id/button" id)
            symbol
            eval))
     (let [that this
           context (.getApplicationContext this)]
       (proxy [android.view.View$OnClickListener] []
         (onClick [view]
           (. that
              startActivity
              (doto (Intent. that jp.backflip180.test.DetailActivity)
                (.putExtra "ID" (int id))))))))))

;; (defactivity jp.backflip180.test.MainActivity
;;   :def a
;;   :on-create
;;   (fn [this bundle]
;;     (on-ui
;;      (set-content-view! a
;;       (make-ui [:linear-layout {}
;;                 [:text-view {:text "Hello from Clojure!"}]])))))

;; (def alphabet
;;   (atom {:type :phonetic
;;          :letters ["alpha" "bravo" "charlie" "delta"]}))

;; (defn make-adapter []
;;   (ref-adapter
;;    (fn [] (neko.ui/make-ui-element a [:text-view {}]
;;                                    {:container-type :abs-listview-layout}))
;;    (fn [position view _ data]
;;      (.setText ^TextView view (str position ". " data)))
;;    alphabet
;;    :letters))

;; (on-ui
;;  (set-content-view!
;;   a
;;   (make-ui [:list-view {:adapter (make-adapter)}])))

;; (swap! alphabet update-in [:letters] conj "echo")

