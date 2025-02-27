(ns api.graphql.document-plan-test
  (:require [api.test-utils :refer [q]]
            [api.db-fixtures :as db-fixtures]
            [clojure.test :refer [deftest is use-fixtures]]
            [data.entities.document-plan :as document-plan]
            [data.entities.user-group :as user-group]))

(use-fixtures :each db-fixtures/clean-db)

(deftest ^:integration write-document-plan
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [id]} :createDocumentPlan} :data errors :errors} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "\"{}\""}})]
    (is (nil? errors))
    (is (string? id))))

(deftest ^:integration document-plans-test
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        _     (q "/_graphql" :post {:query     query
                                    :variables {:uid          "01"
                                                :name         "test"
                                                :kind         "Document"
                                                :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                                :documentPlan "\"{}\""}})
        query "{documentPlans(offset:%s limit:%s){items{id uid name blocklyXml documentPlan dataSampleId dataSampleRow createdAt updatedAt updateCount} offset limit totalCount}}"
        {{{{:keys [items offset limit totalCount]} :documentPlans} :data errors :errors} :body}
        (q "/_graphql" :post {:query (format query 0 20)})]
    (is (nil? errors))
    (is (seq items))
    (is (zero? offset))
    (is (= 20 limit))
    (is (pos-int? totalCount))))

(deftest ^:integration fetch-document-plan-by-id
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [id]} :createDocumentPlan} :data} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "{}"}})
        query "{documentPlan(id:\"%s\"){id uid name blocklyXml documentPlan createdAt updatedAt updateCount}}"
        {{{{:keys [id uid name blocklyXml documentPlan createdAt updatedAt updateCount]} :documentPlan} :data errors :errors} :body}
        (q "/_graphql" :post {:query (format query id)})]
    (is (nil? errors))
    (is (string? id))
    (is (= "01" uid))
    (is (= "test" name))
    (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>" blocklyXml))
    (is (= "{}" documentPlan))
    (is (pos-int? createdAt))
    (is (= createdAt updatedAt))
    (is (zero? updateCount))))

(deftest ^:integration fetch-document-plan-by-name
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [name]} :createDocumentPlan} :data} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "{}"}})
        query "{documentPlan(name:\"%s\"){id uid name blocklyXml documentPlan createdAt updatedAt updateCount}}"
        {{{{:keys [id uid name blocklyXml documentPlan createdAt updatedAt updateCount]} :documentPlan} :data errors :errors} :body}
        (q "/_graphql" :post {:query (format query name)})]
    (is (nil? errors))
    (is (string? id))
    (is (= "01" uid))
    (is (= "test" name))
    (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>" blocklyXml))
    (is (= "{}" documentPlan))
    (is (pos-int? createdAt))
    (is (= createdAt updatedAt))
    (is (zero? updateCount))))

(deftest ^:integration fetch-document-plan-without-args
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [name]} :createDocumentPlan} :data} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "{}"}})
        query "{documentPlan{id uid name blocklyXml documentPlan createdAt updatedAt updateCount}}"
        {{errors :errors} :body}
        (q "/_graphql" :post {:query (format query name)})]
    (is (= "Either document plan `id` or `name` must be provided." (get-in errors [0 :message])))))

(deftest ^:integration create-document-plan-test
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount]} :createDocumentPlan} :data errors :errors} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "02"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "{}"}})]
    (is (nil? errors))
    (is (string? id))
    (is (= "02" uid))
    (is (= "test" name))
    (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>" blocklyXml))
    (is (= "{}" documentPlan))
    (is (nil? dataSampleRow))
    (is (pos-int? createdAt))
    (is (= createdAt updatedAt))
    (is (zero? updateCount))))

(deftest ^:integration update-document-plan-test
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [id]} :createDocumentPlan} :data} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "{}"}})

        query "mutation updateDocumentPlan($id: ID! $uid: ID $name: String $blocklyXml: String $documentPlan: String $dataSampleRow: Int){updateDocumentPlan(id: $id uid: $uid name: $name blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount}}"
        {{{{:keys [id uid name blocklyXml documentPlan dataSampleRow dataSampleId createdAt updatedAt updateCount]} :updateDocumentPlan} :data errors :errors} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:id            id
                                          :uid           "01"
                                          :name          "test-updated"
                                          :blocklyXml    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan  "{}"
                                          :dataSampleRow 0}})]
    (is (nil? errors))
    (is (string? id))
    (is (= "01" uid))
    (is (= "test-updated" name))
    (is (= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>" blocklyXml))
    (is (= "{}" documentPlan))
    (is (zero? dataSampleRow))
    (is (nil? dataSampleId))
    (is (pos-int? createdAt))
    (is (pos-int? updatedAt))
    (is (= 1 updateCount))))

(deftest ^:integration delete-document-plan-test
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        {{{{:keys [id]} :createDocumentPlan} :data} :body}
        (q "/_graphql" :post {:query     query
                              :variables {:uid          "01"
                                          :name         "test"
                                          :kind         "Document"
                                          :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                          :documentPlan "\"{}\""}})

        query "{documentPlan(id:\"%s\"){id uid name blocklyXml documentPlan createdAt updatedAt updateCount}}"]
    (is (some? (-> (q "/_graphql" :post {:query (format query id)})
                   :body
                   :data
                   :documentPlan)))
    (let [{{{response :deleteDocumentPlan} :data errors :errors} :body}
          (q "/_graphql" :post {:query     "mutation deleteDocumentPlan($id: ID!){deleteDocumentPlan(id: $id)}"
                                :variables {:id id}})]
      (is (nil? errors))
      (is (true? response))
      (is (nil? (-> (q "/_graphql" :post {:query (format query id)})
                    :body
                    :data
                    :documentPlan))))))

(deftest ^:integration listing-empty-doc-plans
  (is (= [] (document-plan/list-document-plans user-group/DUMMY-USER-GROUP-ID)))
  (let [query "mutation createDocumentPlan($uid: ID! $name: String! $kind: String $blocklyXml: String! $documentPlan: String! $dataSampleRow: Int){createDocumentPlan(uid: $uid name: $name kind: $kind blocklyXml: $blocklyXml documentPlan: $documentPlan dataSampleRow: $dataSampleRow){ id uid name blocklyXml documentPlan dataSampleRow createdAt updatedAt updateCount }}"
        _     (q "/_graphql" :post {:query     query
                                    :variables {:uid          "01"
                                                :name         "test"
                                                :kind         "Document"
                                                :blocklyXml   "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
                                                :documentPlan "{}"}})]
    (is (= [{:blocklyXml       "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>"
             :dataSampleId     nil
             :dataSampleMethod nil
             :dataSampleRow    nil
             :examples         nil
             :documentPlan     {}
             :name             "test"
             :kind             "Document"
             :uid              "01"
             :updateCount      0}] (map #(dissoc % :createdAt :updatedAt :id) (document-plan/list-document-plans user-group/DUMMY-USER-GROUP-ID))))))
