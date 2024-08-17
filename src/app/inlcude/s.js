import React, { useState } from 'react';

const DateInputComponent = () => {
    const [date, setDate] = useState('');

    const formatDate = (value) => {
        // Remove all non-digit characters
        const cleanValue = value.replace(/\D+/g, '');

        // Format the cleaned value
        const formattedValue = cleanValue.replace(
            /^(\d{2})(\d{2})(\d{0,4}).*/,
            (_, p1, p2, p3) => [p1, p2, p3].filter(Boolean).join('/')
        );

        setDate(formattedValue);
    };

    const handleChange = (e) => {
        formatDate(e.target.value);
    };

    return (
        <div className="form-group">
            <label htmlFor="dob">Date of Birth*</label>
            <input
                id="dob"
                name="ddob"
                type="text"
                placeholder="DD/MM/YYYY"
                className="form-control"
                required
                aria-label="Pass"
                value={date}
                onChange={handleChange}
            />
        </div>
    );
};

export default DateInputComponent;
