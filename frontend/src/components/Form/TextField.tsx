import * as React from 'react'
import { TextField, InputAdornment } from '@mui/material'
import { Controller } from 'react-hook-form'

export interface ICustomTextFieldProps {
  label: string
  name: string
  control: any
  variant?: string
  number?: boolean
  date?: boolean
  multiline?: boolean
  notFull?: boolean
  minVal?: number
  startAdornment?: string

  // rules
  required?: boolean
  min?: string
  max?: string
  minLength?: number
  maxLength?: number
  pattern?: RegExp
  validate?: any

  errors: any
}

export default function CustomTextField (props: ICustomTextFieldProps): JSX.Element {
  const [fieldType, setFieldType] = React.useState('')
  React.useEffect(() => {
    if ((props.number ?? '') !== '') {
      setFieldType('number')
    } else if ((props.date ?? '') !== '') {
      setFieldType('date')
    }
  }, [props.number, props.date])

  return (
        <Controller
            name={props.name}
            control={props.control}
            rules={{
              required: props.required,
              min: props.min,
              max: props.max,
              minLength: props.minLength,
              maxLength: props.maxLength,
              pattern: props.pattern,
              validate: props.validate
            }}
            render={({ field }) => <TextField
                {...field}
                label={props.label}
                variant={props.variant as any || 'standard'} // eslint-disable-line
                type={fieldType}
                fullWidth={!(props.notFull ?? false)}
                margin="normal"
                InputLabelProps={{ shrink: true }}
                InputProps={{ inputProps: { min: (props.minVal ?? 0) }, startAdornment: (props.startAdornment ?? '') !== '' ? <InputAdornment position="start">{props.startAdornment}</InputAdornment> : null }}
                multiline={props.multiline}
                error={!!props.errors[props.name]} // eslint-disable-line
            />}
        />

  )
}
