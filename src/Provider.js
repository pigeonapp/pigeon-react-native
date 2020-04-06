import React, { Children } from 'react';
import PropTypes from 'prop-types';

import Context from './Context';

class Provider extends React.PureComponent {
  render() {
    return (
      <Context.Provider>{Children.only(this.props.children)}</Context.Provider>
    );
  }
}

Provider.propTypes = {
  children: PropTypes.element.isRequired,
};

export default Provider;
