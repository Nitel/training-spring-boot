<template>
    <div>
        <h2>Indication de la marge d'un produit</h2>
        <at-alert v-if="errorMessage" :message="errorMessage" type="error"></at-alert>
        <at-input v-model="inputValue" placeholder="Identiant d'un produit (ex: 2)"></at-input>
        <h3>Marge : {{marginInEuro}}</h3>        
    </div>
</template>

<script>
import _ from 'lodash';
import axios from 'axios';

export default {
    data() {
        return {
            inputValue: "",
            margin: "—",
            errorMessage: null,
        }
    },
    created: function() {
         this.debouncedValue = _.debounce(this.getMargin, 200)
    },
    watch: {
        inputValue: function(val) {
            this.debouncedValue();
        }
    },
    computed: {
        marginInEuro: function() {
            return `${this.margin} €`
        }
    },
    methods: {
        getMargin: function() {
            const parseValue = parseInt(this.inputValue);

            if (isNaN(parseValue) || parseValue < 0)
                return;

            axios
                .get(`http://localhost:9090/Produits/calculerMargeProduit/${parseValue}`)
                .then(res => {
                    this.errorMessage = null
                    this.margin = res.data
                })
                .catch(e => {
                    this.errorMessage = e.message
                    this.margin = '—'
                })
        }
    }
}
</script>

<style>

</style>