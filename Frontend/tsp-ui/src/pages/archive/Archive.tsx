import React, { useState } from 'react';
import axios from 'axios';
import Form from '../../components/form/Form';
import Input from '../../components/input/Input';
import DropdownSelection from '../../components/dropdown/dropdownSelection';
import Button from '../../components/button/Button';
import { Formik } from 'formik';
import * as yup from 'yup';

function Archive() {
  const [formData, setFormData] = useState<any>({
    startDate: '',
    endDate: '',
    selectedDU: 'choose',
  });
  const [apiErrors, setApiError] = useState<string>("");
  

  const validationSchema = yup.object().shape({
    startDate: yup.string().required('Start Date is required'),
    endDate: yup.string().required('End Date is required'),
    selectedDU: yup.string().notOneOf(['choose'], 'Please select a valid DU'),
  });

  const handleSubmit = async (data: any) => {

    try {

      const response = await axios.post('/api/download', {
        startDate: data.startDate,
        endDate: data.endDate,
        selectedDU: data.selectedDU,
      });


    }
    catch (error:any) {
      setApiError(error.response.data.payLoad)
    }
  }
  return (
    <>
      <Formik
        initialValues={formData}
        validationSchema={validationSchema}
        onSubmit={(values, { setSubmitting }) => {
          handleSubmit(values);
          setSubmitting(false);
        }}
      >
        <Form formData={formData} setFormData={setFormData} onSubmit={handleSubmit}>
          <Input
            label="Start Date"
            name="startDate"
            type="date"
            value={formData.startDate}
            onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
          />




          <Input
            label="End Date"
            name="endDate"
            type="date"
            value={formData.endDate}
            onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
          />

          <DropdownSelection
            label="DU"
            name="selectedDU"
            options={[
              { label: 'DATA', value: 'Data' },
              { label: 'DIGITAL', value: 'Digital' },
              { label: 'QE', value: 'QE' },
              { label: 'Core', value: 'Core' },
            ]}
            value={formData.selectedDU}
            onChange={(e) => setFormData({ ...formData, selectedDU: e.target.value })}
          />

          <Button type="submit">Download</Button>
        </Form>
      </Formik>
    </>
  );
}
export default Archive;
