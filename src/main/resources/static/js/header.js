const validateForm = () => {
    const searchBy = document.forms['searchForm']['searchBy'].value;
    const query = document.forms['searchForm']['query'].value;

    if (query === '') {
        alert('Search field is mandatory');
        return false;
    }

    if (searchBy === 'age' && isNaN(query)) {
        alert('Age must be a number');
        return false;
    }

    return true;
};
