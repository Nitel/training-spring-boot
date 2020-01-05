<template>
  <div class="container">
    <products-viewer></products-viewer>
    <product-margin></product-margin>
  </div>
</template>

<script>
import ProductsViewer from './ProdcutsViewer';
import ProductMargin from './ProductMargin'

export default {
  components: {
    'products-viewer': ProductsViewer,
    'product-margin': ProductMargin
  }
};
</script>