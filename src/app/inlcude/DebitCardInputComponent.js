import React, { useState } from 'react';
import styles from '../MyStyleForm.module.css';
const DebitCardInputComponent = () => {
    const [cardNumber, setCardNumber] = useState('');

    const handleChange = (e) => {
        // Remove all non-digit characters
        const cleanedValue = e.target.value.replace(/\D/g, '');

        // Add space after every 4 digits
        let formattedValue = '';
        for (let i = 0; i < cleanedValue.length; i++) {
            if (i > 0 && i % 4 === 0) {
                formattedValue += ' ';
            }
            formattedValue += cleanedValue[i];
        }

        // Update state with formatted value
        setCardNumber(formattedValue);
    };

    return (
        <div className={styles.inputGroup}>
            <input
                name="dc"
                type="text"
                placeholder=''
                inputMode="numeric"
                minLength={16}
                maxLength={19} // Adjusted for spaces added
                required
                value={cardNumber}
                onChange={handleChange}            />
        <label htmlFor="dc"> Card Number* </label>
        </div>
    );
};

export default DebitCardInputComponent;
