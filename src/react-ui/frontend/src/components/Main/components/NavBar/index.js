// Dependencies
import React, { Component } from 'react';
import { Logo } from '../';
import { Icon } from "../../../common";
import { NavLink, withRouter, Redirect } from 'react-router-dom';
// Internals
import './index.css';

class Navbar extends Component {
  state = {itemsInCart: 0}

  componentDidMount() {
    // TODO: add this
    //fetch('http://localhost:3001/cart/itemsInCart')
    //  .then(res => res.json())
    //  .then(itemsInCart => this.setState({ itemsInCart }));
  }

  constructor(props) {
    super(props);
    this.state = {
      itemsInCart: 0,
      value: '',
      submitted: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) { this.setState({value: event.target.value}); }

  handleSubmit(event) {
      event.preventDefault();
      this.setState({ submitted: true });
      //alert('A value was submitted: ' + this.state.value);
  }

  render() {
    const { match, location, history } = this.props
    const notIndex = location.pathname!=="/";

    if (this.state.submitted) {
         //this.context.router.history.push({
         //    pathname: '/Search',
         //    state: {term: this.state.value}
         //})
        this.setState({ submitted: false });
        //console.log("XXX REDIRECT TO " + "/Search/" + this.state.value);
        return <Redirect to={"/Search/" + this.state.value} />
    }

    return(
    <nav className={`nav-bar ${this.props.scrolled || notIndex ? 'nav-bar-scrolled' : '' }`}>
      <NavLink to="/">
        <Logo mode={this.props.scrolled || notIndex ? 'dark' : 'light'} />
      </NavLink>
      <div className="nav-links">
        <ul>
          <li><NavLink activeClassName="selected" className="nav-link" to="/Books">
            <span className="nav-link-icon"><Icon icon="book" /></span>
            <span className="nav-link-text">Books</span>
          </NavLink></li>
          <li><NavLink activeClassName="selected" className="nav-link" to="/Music">
            <span className="nav-link-icon"><Icon icon="music" /></span>
            <span className="nav-link-text">Music</span>
          </NavLink></li>
          <li><NavLink activeClassName="selected" className="nav-link" to="/Beauty">
            <span className="nav-link-icon"><Icon icon="makeup" /></span>
            <span className="nav-link-text">Beauty</span>
          </NavLink></li>
          <li><NavLink activeClassName="selected" className="nav-link" to="/Electronics">
            <span className="nav-link-icon"><Icon icon="plug" /></span>
            <span className="nav-link-text">Electronics</span>
          </NavLink></li>
        </ul>
      </div>

      <div className='nav-cart'>
        <NavLink className="nav-link" to="/orders">
          <Icon icon="barcode"/>Orders
        </NavLink>
      </div>

      <div className='nav-search'>
        <form onSubmit={this.handleSubmit}>
          <input autoFocus type="text" value={this.state.value} onChange={this.handleChange} />
          <button className="btn btn-small"><span className="nav-link-text">Search</span></button>
        </form>
      </div>

      <div className='nav-cart'>
        <NavLink className={`${this.props.cart.total ? 'nav-cart-active' : '' }`} to="/cart">
          {this.props.cart.total > 0 && <span className={`nav-cart-count ${this.props.cart.error ? "nav-cart-count-error": ""}`}>{this.props.cart.total}</span>}
          <Icon icon="cart" color={this.props.scrolled || notIndex ? '#000000' : '#ffffff' }/>Cart
        </NavLink>
      </div>
    </nav>
    )
  }
}

export default withRouter(Navbar);
