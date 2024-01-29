import { PropTypes } from "prop-types";

const Notice = ({ notice }) => {
  return (
    <div className="flex">
      <p className='basis-1/3'>{ notice.created_at }</p>
      <p className='basis-2/3'>{ notice.title }</p>
    </div>
  );
};

Notice.propTypes = {
  notice: PropTypes.object,
};

export default Notice;
