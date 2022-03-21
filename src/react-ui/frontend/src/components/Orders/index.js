//Dependencies
import React, { Component } from 'react';
import { Button } from '../../components/common';
import { Link } from 'react-router-dom';
//Internals
import './index.css';

class Orders extends Component {
  constructor(props) {
    super(props);
    this.state = {
      product: undefined,
      totalCount: 0,
      isCompleted: false
    };
  }

  componentDidMount() {
    //this.updateTimer = setInterval(() => this.fetchOrderDetails(), 5000);
    this.interval = setInterval(() => this.fetchOrderDetails(), 1000);
  }

  componentWillUnmount() {
    //clearInterval(this.updateTimer);
    clearInterval(this.interval);
  }

  costReducer = (accumulator, currentValue) => {
    const self = this;
    return accumulator + currentValue.price * parseInt(self.props.cart.data[currentValue.id] || 0);
  }

  fetchOrderDetails() {
      var url = '/orders';
      console.log("Fetching url: " + url);
      fetch(url)
          .then(res => res.json())
          .then(product => {
            this.setState({ product });
            this.state.totalCount = product.ordercount;
            console.log("@@@ACTEST:"+JSON.stringify(product));
            console.log("@@ACTEST this.state.totalCount:"+this.state.totalCount);
          });
  }
  render() {
    const self = this;
    const currentProduct = this.state.product;
    return(
      <div className="cart-container">
      <div className="container">
        <div className="row">
          <div className="col-12 col-sm-4">
            <div className="card">
              <h5><span>Total Orders: {this.state.totalCount}</span></h5>
            </div>
          </div>
        </div>

      </div>
      </div>
    );
  }
}

export default Orders;
