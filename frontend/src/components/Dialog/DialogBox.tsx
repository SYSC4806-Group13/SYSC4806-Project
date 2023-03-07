import * as React from 'react';
import { Dialog, DialogTitle, DialogContent } from "@mui/material"

export interface IDialogBoxProps {
    isDialogOpen: boolean,
    handleCloseDialog: () => void,
    children?: React.ReactElement | React.ReactElement[]
}

export default function DialogBox(props: IDialogBoxProps) {
    const [open, setOpen] = React.useState(false);

    React.useEffect(() => {
        setOpen(props.isDialogOpen)
    }, [props.isDialogOpen])


    const handleClose = () => {
        setOpen(false);
        props.handleCloseDialog();
    };
    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Add Listing</DialogTitle>
            <DialogContent>
                {props.children}
            </DialogContent>
        </Dialog>
    );
}