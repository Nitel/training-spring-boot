<template>
  <div>
    <h2>Les produits</h2>
    <at-table :columns="columns1" :data="data1" stripe></at-table>
    <at-button class="btn" type="primary" @click="retrivesOrderedProducts">Récupérer les données triées</at-button>
  </div>
</template>

<script>
import axios from "axios";

const parseData = rawData => {
  const groups = rawData.match(
    /Product{id=(\d+), *nom='(.+)', *prix=(\d+)} *: *(\d+)/
  );

  const [, id, name, price, comMargin] = groups;

  return {
    id,
    name,
    price,
    comMargin: `${comMargin} €`
  };
};

export default {
  data() {
    return {
      columns1: [
        {
          title: "Identifiant",
          key: "id"
        },
        {
          title: "Nom",
          key: "name"
        },
        {
          title: "Prix",
          key: "price"
        },
        {
          title: "Marge",
          key: "comMargin"
        }
      ],
      data1: []
    };
  },
  methods: {
    retrivesOrderedProducts: function() {
      axios
        .get(
          "http://localhost:9090/Produits/trierProduitsParOrdreAlphabetique",
          {
            headers: {
              "Access-Control-Allow-Origin": "*"
            }
          }
        )
        .then(res => res.data)
        .then(rawData =>
          rawData.map(
            ({ id, nom: name, prix: price, prixAchat: priceBuy }) => ({ id, name, price, comMargin: `${price - priceBuy} €` })
          )
        )
        .then(products => (this.data1 = products));
    }
  },
  mounted() {
    axios
      .get("http://localhost:9090/AdminProduits/calculerMargeProduit", {
        headers: {
          "Access-Control-Allow-Origin": "*"
        }
      })
      .then(res => res.data)
      .then(rawData => rawData.map(e => parseData(e)))
      .then(products => (this.data1 = products));
  }
};
</script>